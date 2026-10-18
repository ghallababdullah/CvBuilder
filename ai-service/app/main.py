from fastapi import FastAPI

# Создаём приложение (аналог @SpringBootApplication)
app = FastAPI(
    title="CV Builder AI Service",
    description="ML-powered features for CV Builder",
    version="0.1.0"
)


# Простой endpoint — проверка что живо
@app.get("/health")
def health_check():
    """Health check endpoint — аналог /api/health в Java backend"""
    return {"status": "ok", "service": "ai-service"}


# Корневой endpoint
@app.get("/")
def root():
    """Welcome endpoint"""
    return {
        "message": "CV Builder AI Service",
        "docs": "/docs",
        "health": "/health"
    }
