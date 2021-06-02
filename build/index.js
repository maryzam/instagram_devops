
const fs = require('fs');
const { buildPostImage } = require('./buildSteps/buildPostImage');
const { buildPostComment } = require('./buildSteps/buildPostComment');

const argv = require('minimist')(process.argv.slice(2));
const publishFolder = argv.publish;

// TODO grab it from the parameters
const sourceLocation =  argv.source;
const postSource = fs.readFileSync(sourceLocation); 
const postData = JSON.parse(postSource);

fs.mkdir(publishFolder, { recursive: true }, (err) => {
    
    buildPostImage(Object.assign({ targetFolder: publishFolder }, postData.image));

    buildPostComment({
        description: postData.description,
        hashtags: postData.hashtags,
        targetFolder: publishFolder
    });
});
