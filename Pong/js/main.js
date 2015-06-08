/*
 * Game to do:
 * add proper menu
 * paddle color option
 * credits option
 * leader board with html5 storage or rest api?
 * Add 2 player functionality! using arrow keys and taking over ai
 * modularise the code please
 * also add sound :)
 */

// Game Constants:
var WIDTH = 700, HEIGHT = 600, PI = Math.PI;

// Input Keys:
var UP = 87, DOWN = 83;

// Game Globals:
var canvas, ctx, frames;

var keystate, currentState, score1, score2;

var states = { pause : 0, game : 1};

var paddleColor = "#fff";

// Game Objects:

var player = {
	x : null,
	y : null,

	width : 20,
	height : 100,

	update : function() {
		if (keystate[UP] && this.y > 0) { this.y -= 7 };
		if (keystate[DOWN] && (this.y + this.height) < HEIGHT) { this.y += 7 };
	},

	draw : function() {
		ctx.fillRect(this.x, this.y, this.width, this.height);
	}
};

var ai = {
	x : null,
	y : null,

	width : 20,
	height : 100,

	update : function() {
		// track the ball position, so that it will hit the middle of the paddle
		// this.height - ball.height is the are the ball won't hit.
		// half to hit the middle of the paddle
		var ball_destination = ball.y - (this.height - ball.height) * 0.5;
		// ball and paddle will hit only when the positions are ===
		// so ball_position - this.y = 0 then paddle will hit the ball
		this.y += (ball_destination - this.y) * 0.1; // but slow the paddle down by 90%
		// y axis collision detection
		this.y = Math.max(Math.min(this.y, HEIGHT - this.height), 0);
	},

	draw : function() {
		ctx.fillRect(this.x, this.y, this.width, this.height);
	}
};

var ball  = {
	x : null,
	y : null,
	velocity : null,
	speed : 10,

	width : 20,
	height : 20,

	serve : function(side) {
		var r = Math.random();
		this.x = side === 1 ? player.x + player.width : ai.x - this.width;
		this.y = (HEIGHT - this.height) * r;
		var phi = 0.1*PI*(1 - 2*r);
		this.velocity = {
			x : side * this.speed * Math.cos(phi),
			y : this.speed * Math.sin(phi)
		};
	},

	update : function() {
		this.x += this.velocity.x;
		this.y += this.velocity.y;

		// aabb collision detection (ball, paddle)
		var AABB = function(a, b) {
			return a.x < b.x + b.width &&
				   a.y < b.y + b.height &&
				   b.x < a.x + a.width &&
				   b.y < a.y + a.height;
		};

		var paddle = this.velocity.x < 0 ? player : ai; // left player, right ai
		if (AABB(paddle, this)) {
			this.x = paddle === player ? player.x + player.width : ai.x - this.width;
			// normalise the balls position relative to the paddle
			var normalise = (this.y + this.height - paddle.y)/(paddle.height + this.height);
			var phi = 0.25*PI*(2*normalise - 1); // .25 pi is 45 degree.
			var smash = Math.abs(phi) > 0.2*PI ? 1.5 : 1;
			this.velocity.x = smash*(paddle===player ? 1 : -1)*this.speed * Math.cos(phi);
			this.velocity.y = smash*this.speed * Math.sin(phi);
		}

		// ball collision detection:
		if (this.y < 0 || (this.y + this.height) > HEIGHT) {
			var offset = this.velocity.y < 0 ? 0 - this.y : HEIGHT - (this.y + this.height);
			this.y += 2*offset; // makes ball bounce look realistic.
			this.velocity.y *= -1; // reflect or bounce, send ball the other way
		}

		// wall collision condition
		if ((this.x + this.width) < 0 || this.x > WIDTH) {
			score1 += paddle === player ? 0 : 1; // if the ball was going towards player score += 0
			score2 += paddle === ai ? 0 : 1;
			this.serve((paddle===player ? 1 : -1));
		}

	},

	draw : function() {
		ctx.fillRect(this.x, this.y, this.width, this.height);
	}
};

// menu key event
function togglePause(evt) {
	if (evt.keyCode === 32) {
		switch (currentState) {
			case states.game:
				currentState = states.pause;
				break;

			case states.pause:
				currentState = states.game;
				break;
		}
	}
}

// random color generator
function getRandomColor() {
    var letters = '0123456789ABCDEF'.split('');
    var color = '#';
    for (var i = 0; i < 6; i++ ) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

// Game Functions:

function main() {
	canvas = document.createElement("canvas");
	canvas.width = WIDTH;
	canvas.height = HEIGHT;

	ctx = canvas.getContext("2d");
	document.body.appendChild(canvas);

	frames = 0;

	// game input:
	var space = "keydown";
	document.addEventListener(space, togglePause);

	keystate = {};
	document.addEventListener("keydown", function(evt) {
		keystate[evt.keyCode] = true;
	});

	document.addEventListener("keyup", function(evt) {
		delete keystate[evt.keyCode];
	});

	init();
	run();
}

function init() {
	currentState = states.pause;
	score1 = 0;
	score2 = 0;
	// initial positions of ball and paddles
	player.x = player.width; // width offset
	player.y = (HEIGHT - player.height)/2;

	ai.x = WIDTH - (ai.width * 2); // times 2 for width offset and paddle offset.
	ai.y = (HEIGHT - ai.height)/2;

	ball.serve(1);
}

function run() {
	update();
	draw();
	window.requestAnimationFrame(run, canvas);
}

function draw() {
	// draw black backgroud
	ctx.fillRect(0, 0, WIDTH, HEIGHT);

	ctx.font = '72pt Arial';
    ctx.fillStyle = '#2D2E2E';  // was 2D2E2E
    ctx.textBaseline = 'top';
    ctx.fillText(score1, WIDTH*0.2, HEIGHT*0.1);
    ctx.fillText(score2, WIDTH*0.725, HEIGHT*0.1);
    ctx.fill();
    ctx.fillStyle = "#000"; // black background

	ctx.save(); // yes you can save the current context! wow such amazing!

	// color the paddles and ball white.
	//ctx.fillStyle = "#fff";
	ctx.fillStyle = paddleColor;

 	player.draw();
	ai.draw();

	ctx.fillStyle = "#fff";

	ball.draw();

	var _width = 4;
	var _freq = 20;
	var x = (WIDTH - _width)*0.5; // or you can *0.5
	var y = 0;
	var step = HEIGHT/_freq;

	while (y < HEIGHT) {
		// func takes x,y start pos and w,h lengths
		ctx.fillRect(x, y+step*0.25, _width, step*0.5); // legth is half a step
		y += step;
		//console.log("step = " + step + ", y = " + y);
	}


	ctx.restore();

    if (currentState === states.pause) {
    	ctx.font = '24pt Arial';
        ctx.fillStyle = '#fff'; // white
        ctx.textBaseline = 'top';
        ctx.fillText("PAUSED", canvas.width/2 - 65, canvas.height/2 - 35);
        ctx.font = '10pt Arial';
        ctx.fillText("Hit SPACE to unpause", canvas.width/2 - 67, canvas.height/2);
        ctx.fill();
        ctx.fillStyle = "#000"; // black background
    }
}

function update() {
	frames++;
	if (currentState !== states.pause) {
		ball.update();
		player.update();
		ai.update();
	}
}

main();
console.log("Execution Successful.");
