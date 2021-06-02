const { IgApiClient } = require('instagram-private-api');
const { readFile } = require('fs').promises;

const argv = require('minimist')(process.argv.slice(2));
const publishFolder = argv.publish;

const ig = new IgApiClient();

const login = async () => {
    if (process.env.IG_PROXY && process.env.IG_PROXY.length > 0) {
        ig.state.proxyUrl = process.env.IG_PROXY;
    }
    ig.state.generateDevice(process.env.IG_USERNAME);
    await ig.account.login(process.env.IG_USERNAME, process.env.IG_PASSWORD);
    console.log(`Logged in ${process.env.IG_USERNAME} instagram account successfully.`)
};

const getAucklandLocation = async () => {
    const latitude = -36.848461;
    const longitude = 174.763336;
    const placeName = "Auckland";

    const locations = await ig.search.location(latitude, longitude, placeName);
    return locations[0];
};

const publishPost = async () => {
    const publishResult = await ig.publish.photo({
      file: await loadImageFile(),
      caption: await loadComment(),
      location: await getAucklandLocation()
    });
    console.log('**********************');
    console.log('Post is published!');
    console.log(publishResult);
}

const loadImageFile = async () => {
    const imagePath = `${publishFolder}/post_image.jpg`;
    const imageData = await readFile(imagePath);
    return imageData;
};

const loadComment = async () => {
    const commentPath = `${publishFolder}/post_comment.txt`;
    const data = await readFile(commentPath, { encoding: 'utf8'});
    console.log('');
    console.log(data);
    console.log('');
    return data;
}

(async () => {
    await login();
    await publishPost();
})();

