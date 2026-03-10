from fastapi.testclient import TestClient
from app.main import app
import os

client = TestClient(app)

def test_extract_text():
    current_dir = os.path.dirname(os.path.abspath(__file__))
    with open(os.path.join(current_dir, "assets", "test.pdf"), "rb") as f:
        response = client.post("/extract-text", files={"file": ("test.pdf", f, "application/pdf")})
    assert response.status_code == 200
    assert "extracted_text" in response.json()


def test_rank_jobs():
    # 1. Prepare the JSON data exactly like your Java 'PredictionRequest'
    payload = {
        "text": "Java Spring Boot Developer",
        "dics": {
            "1": "Java developer with Spring Boot experience required",
            "2": "Data Scientist using Python and machine learning techniques"
        }
    }
    
    # 2. Mock the POST request
    response = client.post("/rank-jobs", json=payload)
    
    # 3. Assertions
    assert response.status_code == 200
    data = response.json()
    
    # Check if the keys returned match our job IDs
    assert "1" in data
    assert "2" in data
    # Job 1 should have a much higher score than Job 2
    assert data["1"] > data["2"]


def test_rank_candidates():
    # 1. Prepare the JSON data exactly like your Java 'PredictionRequest'
    payload = {
        "text": "Java Spring Boot Developer skills required",
        "dics": {
            "1": "Java Spring Boot Developer",
            "2": "Data Scientist using Python"
        }
    }
    
    # 2. Mock the POST request
    response = client.post("/rank-candidates", json=payload)
    
    # 3. Assertions
    assert response.status_code == 200
    data = response.json()
    
    # Check if the keys returned match our candidate IDs
    assert "1" in data
    assert "2" in data
    # Candidate 1 should have a much higher score than Candidate 2
    assert data["1"] > data["2"]

