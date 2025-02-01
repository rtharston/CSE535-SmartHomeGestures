import os

from flask import Flask, request, abort, send_file
from markupsafe import escape

app = Flask(__name__)
app.config["EXAMPLES_FOLDER"] = 'examples'
app.config["UPLOADS_FOLDER"] = 'uploads'
app.config["GESTURES"] = [
    '0',
    '1',
    '2',
    '3',
    '4',
    '5',
    '6',
    '7',
    '8',
    '9',
    'DecreaseFanSpeed',
    'FanOff',
    'FanOn',
    'IncreaseFanSpeed',
    'LightOff',
    'LightOn',
    'SetThermo',
]


def validate_gesture(gesture):
    if gesture not in app.config["GESTURES"]:
        abort(400, f'invalid gesture {gesture}')


@app.get('/')
def index():
    return """
    <p>Endpoints:</p>
    <ul>
        <li>GET from `/example/<gesture>` to get example videos of gestures</li>
        <li>POST to `/upload/<gesture>` to save videos of gestures</li>
    </ul>
    """


@app.get('/example/<gesture>')
def get_video(gesture):
    validate_gesture(gesture)
    return send_file(os.path.join(app.config["EXAMPLES_FOLDER"], f'{gesture}_example.mp4'))


@app.post('/upload/<gesture>')
def upload_video(gesture):
    validate_gesture(gesture)
    # save a video for this gesture
    if 'video' not in request.files:
        return 400
    f = request.files['video']
    if f:
        if os.path.splitext(f.filename)[-1].lower() != '.mp4':
            return f'only mp4 files supported: {f.filename}', 415
        # TODO: get count of existing videos and increment num
        num = 0
        f.save(os.path.join(app.config["UPLOADS_FOLDER"], f'{gesture}_user_{num}.mp4'))
        return f'Saved video for {escape(gesture)}'
    else:
        return f'Missing video file for {escape(gesture)}'


if __name__ == '__main__':
    from argparse import ArgumentParser
    parser = ArgumentParser(prog='GestureServer')
    parser.add_argument('-d', '--debug', action='store_true')
    args = parser.parse_args()

    app.run(host="0.0.0.0", port=50001, debug=args.debug)