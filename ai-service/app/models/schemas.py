"""
Pydantic schemas for request/response validation.
Analog of DTO classes in Java Spring.
"""

from pydantic import BaseModel, Field
from typing import List


class RecommendRequest(BaseModel):
    """Request schema for /recommend-templates endpoint"""
    
    description: str = Field(
        ...,
        min_length=10,
        max_length=1000,
        description="Description of user's background, skills, experience",
        examples=["Senior Python backend developer with 5 years in fintech"]
    )


class TemplateRecommendation(BaseModel):
    """Single template recommendation with similarity score"""
    
    id: int
    name: str
    category: str
    score: float = Field(
        ...,
        description="Cosine similarity score between 0 and 1. Higher = better match."
    )


class RecommendResponse(BaseModel):
    """Response schema for /recommend-templates endpoint"""
    
    query: str = Field(description="Original user description")
    recommendations: List[TemplateRecommendation] = Field(
        description="Top-N most relevant templates, sorted by score descending"
    )
    count: int = Field(description="Number of recommendations returned")
