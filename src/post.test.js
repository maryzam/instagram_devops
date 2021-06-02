const fs = require('fs');
const d3 = require('d3-color');

const postSource = fs.readFileSync('./src/post.json');
const post = JSON.parse(postSource);

describe("Post has a valid image info:", () => {

    test("Image text is provided.", () => {
        expect(post.image.text).not.toBe(undefined);
        expect(post.image.text.length).toBeGreaterThan(0);
    });

    test("Image text in not longer that 20 words.", () => {
        const words = post.image.text.split(' ');
        expect(words.length).toBeLessThanOrEqual(20);
    });

    test("Image text doesn't have more that 5 lines", () => {
        const lines = post.image.text.split('\n');
        expect(lines.length).toBeLessThanOrEqual(5);
    });

    test("Image note in not longer that 10 words.", () => {
        const words = (post.image.note || '').split(' ');
        expect(words.length).toBeLessThanOrEqual(10);
    });

    test("Backgorund color can be converted to HSL.", () => {
        const color = d3.hsl(post.image.backgroundColor);
        expect(color).not.toBe(undefined);
        expect(color.h).not.toBe(undefined);
        expect(color.h).toBeGreaterThanOrEqual(0);
        expect(color.h).toBeLessThanOrEqual(360);
    });

    test("Image section doesn't have any odd parameters.", () => {
        const imageProperties = Object.keys(post.image);
        const knownProperties = new Set(["backgroundColor", "text", "note"]);
        imageProperties.forEach(property => {
            const isKnownProperty = knownProperties.has(property);
            expect(isKnownProperty).toBe(true);
        });
    });
});

describe("Post has a nice description:", () => {

    test("Post description is provided.", () => {
        expect(post.description).not.toBe(undefined);
        expect(post.description.length).toBeGreaterThan(0);
    });

    test("Post description is not longer that 1000 characters.", () => {
        expect(post.description.length).toBeLessThanOrEqual(1000);
    });

    test("Post description is not longer that 50 words.", () => {
        const words = post.description.split(' ');
        expect(words.length).toBeLessThanOrEqual(50);
    });
});

describe("Post has nice set of hastags:", () => {

    test("Post hashtags are provided.", () => {
        expect(post.hashtags).not.toBe(undefined);
        expect(Array.isArray(post.hashtags)).toBe(true);
    });

    test("All hashtags are valid.", () => {
        post.hashtags.forEach(hashtag => {
            expect(hashtag.length).toBeGreaterThan(2);
            expect(hashtag[0]).toBe('#');
            expect(hashtag.match(/#/g).length).toBe(1);
        });
    });
    
    test("There is no more that 30 hashtags.", () => {
        expect(post.hashtags.length).toBeLessThanOrEqual(30);
    });

    test("There is at least 3 hashtags.", () => {
        expect(post.hashtags.length).toBeGreaterThanOrEqual(3);
    });

    test("There is no duplicate hashtags.", () => {
        const lowerCaseHashtags = post.hashtags.map(tag => tag.toLowerCase())
        const uniqueHashtags = new Set(lowerCaseHashtags);
        expect(post.hashtags.length).toBe(uniqueHashtags.size);
    });
});

test("Post has only needed info", () => {
    const knownSections = new Set(["image", "description", "hashtags"]);
    Object.keys(post).forEach(section => {
        const isExpectedSection = knownSections.has(section);
        expect(isExpectedSection).toBe(true);
    });
});