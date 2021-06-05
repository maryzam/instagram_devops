const fs = require('fs');

const buildPostComment = ({
    description,
    hashtags,
    targetFolder
}) => {

    const content = `${description}
    
${hashtags.join(' ')}`;

    fs.writeFile(`${targetFolder}/post_comment.txt`, content, logError)
};

const logError = (error) => {
    if (error) {
        console.error(error);
        throw error;
    }
};

module.exports.buildPostComment = buildPostComment;