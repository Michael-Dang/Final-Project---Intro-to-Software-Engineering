import numpy
import tensorflow
from PIL import Image
import base64
from io import BytesIO
from flask import Flask
from flask import request
app = Flask(__name__)


###### Initialization code - we only need to run this once and keep in memory.
sess = tensorflow.Session()
saver = tensorflow.train.import_meta_graph('saved_model/model_epoch5.ckpt.meta')
saver.restore(sess, tensorflow.train.latest_checkpoint('saved_model/'))
graph = tensorflow.get_default_graph()
x_input = graph.get_tensor_by_name('Input_xn/Placeholder:0')
keep_prob = graph.get_tensor_by_name('Placeholder:0')
class_scores = graph.get_tensor_by_name("fc8/fc8:0")
######

@app.route('/analyze', methods=['GET', 'POST'])
def analyze():
    if request.method == 'POST':
        ret = "NET: "
        for img in request.form.getlist('b64image'):
            ret = ret + scan_image(img) + ' '
        return ret


# Work in RGBA space (A=alpha) since png's come in as RGBA, jpeg come in as RGB
# so convert everything to RGBA and then to RGB.
def scan_image(b64string):
    image = Image.open(BytesIO(base64.b64decode(b64string))).convert('RGB')
    image = image.resize((227, 227), Image.BILINEAR)
    img_tensor = [np.asarray(image, dtype=np.float32)]

    #Run the image in the model.
    scores = sess.run(class_scores, {x_input: img_tensor, keep_prob: 1.})
    return str(scores[0]) + ' ' + str(scores[1])

if __name__ == "__main__":
    app.debug = True
    app.run()