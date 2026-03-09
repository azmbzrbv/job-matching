from fastapi import FastAPI, UploadFile, File
from app.models import RankRequest
from app.services import extractor, matcher


app = FastAPI()


@app.post("/extract-text")
async def extract_text(file: UploadFile = File(...)):
    content = await file.read()
    text = extractor.extract_text_from_pdf(content)
    return {"extracted_text": text}


@app.post("/rank-jobs")
async def rank_jobs(request: RankRequest):
    return matcher.calculate_similarity(request.resume_text, request.jobs)