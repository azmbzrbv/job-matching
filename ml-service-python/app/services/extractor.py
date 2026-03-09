import pdfplumber
import io

def extract_text_from_pdf(pdf_data: bytes) -> str:
    """
    Extracts text from a PDF file given its byte content.
    """
    with pdfplumber.open(io.BytesIO(pdf_data)) as pdf:
        text = ""
        for page in pdf.pages:
            page_text = page.extract_text()
            if page_text:
                text += page_text + "\n"
                
    #remove extra whitespace
    return text.strip()