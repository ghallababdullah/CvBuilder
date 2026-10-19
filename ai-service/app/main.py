"""
CV Builder AI Service - main application.
"""

import logging
from fastapi import FastAPI, HTTPException

from app.models.schemas import RecommendRequest, RecommendResponse, TemplateRecommendation
from app.services.embedding_service import embedding_service

# Setup logging
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(name)s - %(message)s"
)
logger = logging.getLogger(__name__)


# Create FastAPI app
app = FastAPI(
    title="CV Builder AI Service",
    description="ML-powered features for CV Builder",
    version="0.2.0"
)


@app.get("/health")
def health_check():
    """Health check endpoint"""
    return {
        "status": "ok",
        "service": "ai-service",
        "model": embedding_service.MODEL_NAME
    }


@app.get("/")
def root():
    """Welcome endpoint"""
    return {
        "message": "CV Builder AI Service",
        "docs": "/docs",
        "health": "/health",
        "features": ["template-recommendation"]
    }


@app.post(
    "/recommend-templates",
    response_model=RecommendResponse,
    summary="Recommend resume templates",
    description="Returns top-3 most relevant resume templates based on user description. "
                "Uses semantic search via sentence embeddings."
)
def recommend_templates(request: RecommendRequest) -> RecommendResponse:
    """
    Recommend resume templates based on user description.
    
    Example request:
```json
    {"description": "Senior Python backend developer with 5 years in fintech"}
```
    """
    try:
        recommendations = embedding_service.recommend_templates(
            user_description=request.description,
            top_n=3
        )
        
        return RecommendResponse(
            query=request.description,
            recommendations=[
                TemplateRecommendation(**rec) for rec in recommendations
            ],
            count=len(recommendations)
        )
    except Exception as e:
        logger.error(f"Error recommending templates: {e}", exc_info=True)
        raise HTTPException(status_code=500, detail="Internal error during recommendation")
