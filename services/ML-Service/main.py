import google.genai as genai
from google.genai.types import TuningDataset

client = genai.Client(api_key="AIzaSyDR9jsmz5V1xOghXiE4XtSXSM8iWRiNccg")

training_dataset = TuningDataset(
    gcs_uri=""
)

client.tunings.tune(
    base_model="gemini-2.5-flash",
    training_dataset="gemini-2.5-flash",
)
response = client.models.generate_content(
    model="gemini-2.5-flash",
    contents="hello how are you",
)

print(response.text)
