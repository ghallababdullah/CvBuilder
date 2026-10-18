# CV Builder AI Service

ML-powered features for CV Builder application.

## Features (planned)
- Semantic template recommendation (RAG)
- AI resume coach (conversational agent)
- Smart resume review

## Tech Stack
- **FastAPI** - Web framework
- **sentence-transformers** - Embeddings
- **pgvector** - Vector database (via PostgreSQL)
- **LangChain** - AI agents framework

## Setup

```bash
# Create virtual environment
python3 -m venv venv
source venv/bin/activate

# Install dependencies
pip install -r requirements.txt

# Run service
uvicorn app.main:app --reload --port 8000
```

## Endpoints

- `GET /` - Welcome
- `GET /health` - Health check
- `GET /docs` - Swagger UI
- `GET /redoc` - ReDoc

## Communication with Java Backend

AI Service runs on port **8000**.
Java Backend runs on port **8080**.
Communication via REST (upgrade to Kafka planned).
