from app.services.matcher import calculate_similarity, clean_text

def test_clean_text():
    text = "Hello, World! http://example.com"
    cleaned = clean_text(text)
    assert cleaned == "hello world"



def test_calculate_similarity():
    resume_text = "Experienced software engineer with expertise in Python and machine learning."
    jobs = {
        1: "Looking for a software engineer with experience in Python and machine learning.",
        2: "Seeking a data analyst proficient in SQL and Excel.",
        3: "Hiring a software developer skilled in Java and cloud technologies."
    }
    
    results = calculate_similarity(resume_text, jobs)
    
    assert isinstance(results, dict), "Results should be a dictionary"
    assert len(results) == len(jobs), "Results should have the same number of entries as jobs"
    
    # Check that the scores are between 0 and 1
    for score in results.values():
        assert 0 <= score <= 1, "Similarity scores should be between 0 and 1"

    print("Similarity Scores:", results)  # Debug print to see the similarity scores
    # The first job should have the highest similarity score
    assert results[1] > results[2] and results[1] > results[3], "Job 1 should have the highest similarity score"