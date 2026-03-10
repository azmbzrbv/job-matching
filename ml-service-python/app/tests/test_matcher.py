from app.services.matcher import calculate_similarity, clean_text, rank_jobs_for_candidate, rank_candidates_for_job

def test_clean_text():
    text = "Hello, World! http://example.com"
    cleaned = clean_text(text)
    assert cleaned == "hello world"



def test_rank_jobs_for_candidate():
    resume_text = "Experienced software engineer with expertise in Python and machine learning."
    jobs = {
        1: "Looking for a software engineer with experience in Python and machine learning.",
        2: "Seeking a data analyst proficient in SQL and Excel.",
        3: "Hiring a software developer skilled in Java and cloud technologies."
    }
    
    results = rank_jobs_for_candidate(resume_text, jobs)
    
    assert isinstance(results, dict), "Results should be a dictionary"
    assert len(results) == len(jobs), "Results should have the same number of entries as jobs"
    
    # Check that the scores are between 0 and 1
    for score in results.values():
        assert 0 <= score <= 1, "Similarity scores should be between 0 and 1"

    print("Ranked Jobs for Candidate:", results)  # Debug print to see the ranked jobs
    # The first job should have the highest similarity score
    assert results[1] > results[2] and results[1] > results[3], "Job 1 should have the highest similarity score"


    

def test_rank_candidates_for_job():
    job_description = "Looking for a software engineer with experience in Python and machine learning."
    candidate_resumes = {
        1: "Experienced software engineer with expertise in Python and machine learning.",
        2: "Data analyst proficient in SQL and Excel.",
        3: "Software developer skilled in Java and cloud technologies."
    }
    
    results = rank_candidates_for_job(job_description, candidate_resumes)
    
    assert isinstance(results, dict), "Results should be a dictionary"
    assert len(results) == len(candidate_resumes), "Results should have the same number of entries as candidate resumes"
    
    # Check that the scores are between 0 and 1
    for score in results.values():
        assert 0 <= score <= 1, "Similarity scores should be between 0 and 1"

    print("Ranked Candidates for Job:", results)  # Debug print to see the ranked candidates
    # The first candidate should have the highest similarity score
    assert results[1] > results[2] and results[1] > results[3], "Candidate 1 should have the highest similarity score" 