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


def rank_candidates_for_job(job_description: str, candidate_resumes: Dict[int, str]) -> Dict[int, float]:
    """
    Recruiter Logic: 1 Job vs Many CVs.
    Returns {candidate_id: match_score}
    """
    # This is exactly the same logic as calculate_similarity!
    return calculate_similarity(job_description, candidate_resumes)


def rank_jobs_for_candidate(resume_text: str, jobs: Dict[int, str]) -> Dict[int, float]:
    """
    Candidate Logic: 1 CV vs Many Jobs.
    Returns {job_id: match_score}
    """
    # This is exactly the same logic as calculate_similarity!
    return calculate_similarity(resume_text, jobs)




def calculate_similarity(text: str, dics: Dict[int, str]) -> Dict[int, float]:
    """
    Compares a text against a dictionary of texts.
    Returns a dictionary of {id: score}.
    """
    if not dics:
        return {}

    clean_resume = clean_text(text)
    ids = list(dics.keys())
    descriptions = [clean_text(dics[item_id]) for item_id in ids]
    all_texts = [clean_resume] + descriptions



    # 'english' stop words removes common words like 'a', 'the', 'is'
    vectorizer = TfidfVectorizer(stop_words='english')
    tfidf_matrix = vectorizer.fit_transform(all_texts)

    # Compare the first vector (Resume) against all other vectors (Jobs)
    # tfidf_matrix[0:1] is the resume
    # tfidf_matrix[1:] are all the jobs
    similarities = cosine_similarity(tfidf_matrix[0:1], tfidf_matrix[1:])[0]

    results = {
        ids[i]: round(float(similarities[i]), 4) 
        for i in range(len(ids))
    }

    return results