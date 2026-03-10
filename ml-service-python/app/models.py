from pydantic import BaseModel
from typing import Dict

class RankRequest(BaseModel):
    text: str  #resume text or job description
    items: Dict[int, str] #dictionary of {id: text} for jobs or candidates