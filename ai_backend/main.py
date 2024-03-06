# fastapi
from fastapi import FastAPI, UploadFile
from starlette.responses import FileResponse

# insightface
import insightface
from insightface.app import FaceAnalysis
import matplotlib.pyplot as plt


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

######################################################################
######################################################################
# 함수 예시
# @app.get("/")
# def read_root():
#     return {"Hello": "World"}


# @app.get("/items/{item_id}")
# def read_item(item_id: int, q: Union[str, None] = None):
#     return {"item_id": item_id, "q": q}
######################################################################
######################################################################


@app.post("/")
def process_image(userImage: UploadFile):
    # bg_faces = faceswap_app.get(bg_0_img)
    # bg_face = bg_faces[0]

    # faces = faceswap_app.get(userImage)
    # source_face = faces[0]

    # processed_image = bg_0_img.copy()
    # processed_image = swapper.get(bg_0_img, bg_face, source_face, paste_back=True)

    # return {"processedImage": processed_image}
    return {"userImage": userImage}  # 이미지 정보 확인 디버깅용
