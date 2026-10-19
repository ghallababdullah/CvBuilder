"""
Service for computing text embeddings and finding similar templates.
Uses sentence-transformers library for semantic search.
"""

import logging
from typing import List, Dict
import numpy as np
from sentence_transformers import SentenceTransformer

from app.data.templates import TEMPLATES

logger = logging.getLogger(__name__)


class EmbeddingService:
    """
    Handles semantic search over templates using embeddings.
    
    How it works:
    1. On startup: compute embeddings for all templates (one time)
    2. On query: compute embedding for user description
    3. Calculate cosine similarity with all template embeddings
    4. Return top-N most similar templates
    """
    
    MODEL_NAME = "all-MiniLM-L6-v2"
    
    def __init__(self):
        logger.info(f"Loading embedding model: {self.MODEL_NAME}")
        self.model = SentenceTransformer(self.MODEL_NAME)
        logger.info("Model loaded successfully")
        
        logger.info(f"Computing embeddings for {len(TEMPLATES)} templates...")
        self.template_embeddings = self._compute_template_embeddings()
        logger.info("Template embeddings computed and cached")
    
    def _compute_template_embeddings(self) -> np.ndarray:
        """
        Compute embeddings for all templates once at startup.
        Returns numpy array of shape (n_templates, embedding_dim).
        """
        texts = [t["description"] for t in TEMPLATES]
        embeddings = self.model.encode(texts, convert_to_numpy=True)
        return embeddings
    
    def recommend_templates(self, user_description: str, top_n: int = 3) -> List[Dict]:
        """
        Find top-N most similar templates for user description.
        
        Args:
            user_description: Text describing user's background
            top_n: How many top matches to return (default 3)
        
        Returns:
            List of dicts with template info and similarity score
        """
        logger.info(f"Finding templates for query: '{user_description[:50]}...'")
        
        # Step 1: Encode user description to embedding
        query_embedding = self.model.encode(user_description, convert_to_numpy=True)
        
        # Step 2: Compute cosine similarity with all templates
        similarities = self._cosine_similarity(query_embedding, self.template_embeddings)
        
        # Step 3: Get top-N indices (sorted by score descending)
        top_indices = np.argsort(similarities)[::-1][:top_n]
        
        # Step 4: Build results
        results = []
        for idx in top_indices:
            template = TEMPLATES[idx]
            results.append({
                "id": template["id"],
                "name": template["name"],
                "category": template["category"],
                "score": float(similarities[idx])
            })
        
        logger.info(f"Found {len(results)} recommendations, top score: {results[0]['score']:.3f}")
        return results
    
    @staticmethod
    def _cosine_similarity(query: np.ndarray, templates: np.ndarray) -> np.ndarray:
        """
        Compute cosine similarity between query vector and all template vectors.
        
        Formula: cos(θ) = (A · B) / (||A|| * ||B||)
        """
        # Normalize vectors (make them unit length)
        query_norm = query / np.linalg.norm(query)
        templates_norm = templates / np.linalg.norm(templates, axis=1, keepdims=True)
        
        # Dot product = cosine similarity for normalized vectors
        similarities = np.dot(templates_norm, query_norm)
        return similarities


# Singleton instance (создаётся один раз при импорте)
embedding_service = EmbeddingService()
