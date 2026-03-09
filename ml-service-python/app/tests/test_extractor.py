import os
from app.services.extractor import extract_text_from_pdf

def test_extract_text_from_pdf():
    '''
     This test checks if the function correctly extracts text from a PDF file,
     and handles the byte input as expected.
    '''
    # Path to a sample PDF file for testing
    current_dir = os.path.dirname(__file__)
    sample_pdf_path = os.path.join(current_dir, "assets", "test.pdf")
    
    # Read the PDF file as bytes
    with open(sample_pdf_path, 'rb') as f:
        pdf_data = f.read()
    
    # Extract text from the PDF
    extracted_text = extract_text_from_pdf(pdf_data)
    print("Extracted Text:", extracted_text)  # Debug print to see the extracted text

    # Assert that the extracted text is not empty
    assert extracted_text, "Extracted text should not be empty"
    
    # Optionally, you can check for specific content in the extracted text
    assert "Samira" in extracted_text, "Expected content not found in extracted text"