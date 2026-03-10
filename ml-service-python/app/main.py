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
    return matcher.rank_jobs_for_candidate(request.text, request.items)

@app.post("/rank-candidates")
async def rank_candidates(request: RankRequest):
    return matcher.rank_candidates_for_job(request.text, request.items)