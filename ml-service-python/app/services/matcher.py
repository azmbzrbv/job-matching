from sklearn.metrics.pairwise import cosine_similarity
from sklearn.feature_extraction.text import TfidfVectorizer
from typing import Dict
import re


def clean_text(text: str) -> str:
    """
    Basic text cleaning function.
    Can be expanded to include more complex cleaning steps such lemmatization, stop word removal, etc.
    """
    text = text.lower()
    text = re.sub(r'http\S+', '', text)  # Remove URLs
    text = re.sub(r'[^a-zA-Z0-9\s]', '', text)  # Remove punctuation
    text = re.sub(r'\s+', ' ', text)  # Replace multiple spaces with a single space
    return text.strip()


def calculate_similarity(resume_text: str, jobs: Dict[int, str]) -> Dict[int, float]:
    """
    Compares a resume against a dictionary of jobs.
    Returns a dictionary of {job_id: score}.
    """
    if not jobs:
        return {}

    clean_resume = clean_text(resume_text)
    job_ids = list(jobs.keys())
    job_descriptions = [clean_text(jobs[job_id]) for job_id in job_ids]
    all_texts = [clean_resume] + job_descriptions



    # 'english' stop words removes common words like 'a', 'the', 'is'
    vectorizer = TfidfVectorizer(stop_words='english')
    tfidf_matrix = vectorizer.fit_transform(all_texts)

    # Compare the first vector (Resume) against all other vectors (Jobs)
    # tfidf_matrix[0:1] is the resume
    # tfidf_matrix[1:] are all the jobs
    similarities = cosine_similarity(tfidf_matrix[0:1], tfidf_matrix[1:])[0]

    results = {
        job_ids[i]: round(float(similarities[i]), 4) 
        for i in range(len(job_ids))
    }

    return results