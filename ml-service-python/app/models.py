from pydantic import BaseModel
from typing import Dict

class RankRequest(BaseModel):
    text: str
    dics: Dict[int, str] 