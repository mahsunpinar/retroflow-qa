## The Test Engineer Day theory deck

These are the slides for the short theory parts of the Test Engineer day: the kick-off, the first 15 minutes of Block 1, 2 and 3, and the wrap-up. We use [Slidev](https://sli.dev) for them, which means the whole deck is just one markdown file: `slides.md`. Want to change a slide? Edit the markdown, that's it.

### Running the deck

You'll need Node.js (version 20 or higher) for this.

* Open a terminal in this folder and run `npm install`. You only have to do this once.
* Run `npm run dev`. Your browser opens the deck at http://localhost:3030.
* Press `f` for fullscreen, and use the arrow keys to go through the slides.

> Want to see your speaker notes while you present? Open the presenter view at http://localhost:3030/presenter on your own screen, and put the normal view on the projector. The notes are written for coaches, so read them before the day, they tell you what to stress and what not to give away.

### Exporting or hosting it

* `npm run build` creates a static website in the `dist` folder. You can host that anywhere, so other coaches can present from a URL without installing anything.
* `npm run export` creates a PDF (`slides-export.pdf`). The first time, run `npx playwright install chromium` first, Slidev needs it to render the slides.

### Changing the deck

Every slide is separated by `---`. A few things you'll come across in `slides.md`:

* The text inside `<!-- ... -->` at the bottom of a slide is the speaker note.
* `layout: section` gives you a dark title slide (we use it for the start of each block), `class: statement` the orange one.
* The colours and fonts live in `style.css`, the same file as the FA and developer decks. The small orange label above a title is a `<div class="eyebrow">`, the grey line at the bottom is a `<div class="footer">`.

Please keep the course rule in mind when you edit: no em dashes, anywhere.
