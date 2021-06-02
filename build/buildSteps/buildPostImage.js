const fs = require('fs');
const d3 = require('d3-color');
const { createCanvas, loadImage } = require('canvas');

const buildPostImage = ({ targetFolder, backgroundColor, text, note, size = 1080 }) => {

    const canvas = createCanvas(size, size);
    const context = canvas.getContext('2d');

    generateBackground({
        context,
        backgroundColor,
        size
    });

    drawTextOnTop({
        context,
        text,
        note,
        imageSize: size
    });

    saveGeneratedImage(canvas, targetFolder);
}

const generateBackground = ({ context, backgroundColor, size }) => {
    
    const baseColorHue = d3.hsl(backgroundColor).h;

    const gradient = context.createLinearGradient(size*1/3, 0, size, size*2/3);
    gradient.addColorStop(0, `hsl(${baseColorHue}, 81%, 50%)` );
    gradient.addColorStop(1, `hsl(${baseColorHue}, 81%, 5%)` );
    context.fillStyle = gradient;
    context.fillRect(0, 0, size, size);

    const circleCount = Math.floor(20 + Math.random() * 80);
 
    Array.apply(null, Array(circleCount)).map(function (_, i) {
        const value = Math.random()*30 + 20;  
        const opacity = Math.random()*0.25+0.15;
        context.fillStyle = `hsl( ${baseColorHue}, 86%, ${value}%, ${opacity})`;
        
        context.beginPath();
        const cx = (0.1 + Math.random()*0.8)*size;
        const cy = (0.1 + Math.random()*0.8)*size;
        const rad = Math.random()*size*0.1;

        context.arc(cx, cy, rad, 0,2*Math.PI, false);
        context.fill();
    });

    return context.canvas;
}

const drawTextOnTop = ({ context, text, note, imageSize }) => {
    if (!text || !text.length) {
        return;
    }

    drawMultilineText({
        context,
        text,
        options: {
            textAlign: 'end',
            textColor: '#fff',
            maxLineSize: imageSize * 1.5,
            startsAt: { x: imageSize * 0.9, y: imageSize * 0.1 }
        }
    });

    drawMultilineText({
        context,
        text: note,
        options: {
            textAlign: 'start',
            textColor: '#d1d1d1',
            maxLineSize: imageSize,
            startsAt: { x: imageSize * 0.1, y: imageSize * 0.9 }
        }
    });

}

const drawMultilineText = ({ context, text, options }) => {
    if (!text || !text.length) {
        return;
    }

    const lines = text.split("\n");
    const lineMaxSize = lines
                        .map(line => line.length)
                        .reduce((max, len) => Math.max(max, len), 0);
                        
    const fontSize = Math.floor(options.maxLineSize / lineMaxSize);

    context.textAlign = options.textAlign;
    context.fillStyle = options.textColor;
    context.textBaseline = 'top';
    context.font = `bold ${fontSize}px Roboto`;

    const lineHeight = context.measureText("M").width * 1.2;

    let { x, y } = options.startsAt;
    for (let i = 0; i < lines.length; ++i) {
      context.fillText(lines[i], x, y);
      y += lineHeight;
    }
}

const saveGeneratedImage = (canvas, targetFolder) => {
    const buffer = canvas.toBuffer('image/jpeg');
    fs.writeFileSync(`${targetFolder}/post_image.jpg`, buffer);
}

module.exports.buildPostImage = buildPostImage;