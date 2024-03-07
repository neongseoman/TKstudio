# fastapi
from fastapi import FastAPI, UploadFile

# from fastapi.responses import FileResponse
# from pydantic import BaseModel

# insightface
import insightface
from insightface.app import FaceAnalysis
import matplotlib.pyplot as plt

# for test
# from PIL import Image
# import numpy as np


app = FastAPI()

# face swap app
faceswap_app = FaceAnalysis(name="buffalo_l")
faceswap_app.prepare(ctx_id=0, det_size=(640, 640))

# swapper model
swapper_path = "./inswapper_128.onnx"
swapper = insightface.model_zoo.get_model(
    swapper_path, download=False, download_zip=False
)

# img dir
bg_0_img_path = "./bg_0.jpg"
bg_0_img = plt.imread(bg_0_img_path)

# to run the server
# uvicorn main:app --reload


@app.get("/")
def hello():
    return "hello"


# @app.post("/")
# async def process_image(userImage: UploadFile):
#     bg_faces = faceswap_app.get(bg_0_img)
#     bg_face = bg_faces[0]

#     faces = faceswap_app.get(userImg)
#     source_face = faces[0]

#     processed_image = bg_0_img.copy()
#     processed_image = swapper.get(bg_0_img, bg_face, source_face, paste_back=True)

#     return {"processedImage": processed_image}
