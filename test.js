
var n = 0

var smiley = loadimage('smiley.png')

fps(60)
title('Hello World')

function tick() {
    n++
    clear()
    push()
    fill(0x00aa00)
    for (var i = 0; i < 10; i++) {
        text('Hello dude!', 0, i * 16)
    }
    fill(0xff0000)
    translate(mouseX, mouseY)
    scale(0.2, 0.2)
    for (var i = 0; i < 10; i++) {
        for (var j = 0; j < 10; j++) {
            image(smiley, i * (smiley.width + 10), j * (smiley.height + 10))
        }
    }
    pop()
}
