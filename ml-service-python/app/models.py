from pydantic import BaseModel
from typing import Dict

class RankRequest(BaseModel):
    resume_text: str
    jobs: Dict[int, str] 