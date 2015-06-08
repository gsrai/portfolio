// main.js by Gagondeep Srai
/*
 * Notes to self:
 * trailing commas are lazy. plus IE treats them differently to chrome.
 * {x : x, y : y} is a position object
 */

// Constants:
var COLUMNS = 30, ROWS = 30;

// key codes:
var KEY_LEFT = 65,
	KEY_RIGHT = 68,
	KEY_UP = 87,
	KEY_DOWN = 83;

// directions
var Direction = {
	LEFT : 0, RIGHT : 1, UP : 2, DOWN : 3
};

// Id's
var Ids = {
	EMPTY : 0, SNAKE : 1, FRUIT : 2
};

// Grid
var Grid = {
	width : null,
	height : null,
	grid : null,

	init : function(d, columns, rows) {
		this.width = rows;
		this.height = columns;
		this.grid = [];

		for (var x = 0; x < this.width; x++) {
			// each row has an array of elements representing a column.
			this.grid.push([]);
			for (var y = 0; y < this.height; y++) {
				// fill column of row x with a default value d.
				this.grid[x].push(d);
			}
		}

	},

	get : function(x, y) {
		return this.grid[x][y];
	},

	set : function(value, x, y) {
			this.grid[x][y] = value;  // value is empty, fruit or snake
	}
};

// snake is basically a queue, you insert directions to the back of the queue.
var Snake = {
	direction : null,
	queue : null,
	last : null,

	init : function(d, x, y) {
		this.direction = d;
		this.queue = [];
		this.insert(x,y);
	},

	insert : function(x, y) {
		this.queue.unshift({x : x, y : y});
		this.last = this.queue[0];
	},

	remove : function() {
		return this.queue.pop();
	}
};

// game variables:
var canvas, ctx, keystate, frames, score, speed, currentState;

var states = { game : 0, pause : 1, death : 2};

function genFood() {
	var empty = [];

	// find empty areas so you can spawn food in that area.
	for (var x = 0; x < Grid.width; x++) {
		for (var y = 0; y < Grid.height; y++) {
			if (Grid.get(x,y) === Ids.EMPTY) {
				empty.push({x : x, y : y});
			}
		}
	}
	// 0 <= r < number of empty grid elements
	var rand_pos = empty[Math.floor(Math.random() * empty.length)];

	Grid.set(Ids.FRUIT, rand_pos.x, rand_pos.y);
}

function init() {
	currentState = states.pause;
	Grid.init(Ids.EMPTY, ROWS, COLUMNS);
	score = 0;
	speed = 8;
	var start_pos = {x : Math.floor(ROWS/2), y : COLUMNS - 1};
	Snake.init(Direction.UP, start_pos.x, start_pos.y);
	Grid.set(Ids.SNAKE, start_pos.x, start_pos.y);
	genFood();
}

function togglePause(event) {
	if (event.keyCode === 32) {
		switch (currentState) {
			case states.game:
				currentState = states.pause;
				break;

			case states.pause:
				currentState = states.game;
				break;

			case states.death:
				currentState = states.game;
				return init();
				break;
		}
	}
}

function main() {
	canvas = document.createElement("canvas");
	canvas.width = ROWS * 20;
	canvas.height = COLUMNS * 20;

	ctx = canvas.getContext("2d");
	document.body.appendChild(canvas);

	frames = 0;
	keystate = {};

	var space = "keydown";
	document.addEventListener(space, togglePause);

	document.addEventListener("keydown", function(evt) {
		keystate[evt.keyCode] = true;
	});

	document.addEventListener("keyup", function(evt) {
		delete keystate[evt.keyCode];
	});

	init();
	run();
}

function run() {
	update();
	draw();
	window.requestAnimationFrame(run, canvas);
}

function draw() {
	var tile_width = canvas.width/Grid.width;
	var tile_height = canvas.height/Grid.height;

	for (var x = 0; x < Grid.width; x++) {
		for (var y = 0; y < Grid.height; y++) {
			switch (Grid.get(x,y)) {
				case Ids.EMPTY:
					ctx.fillStyle = "#000"; // black cells
					break;
				case Ids.SNAKE:
					ctx.fillStyle = "#0f0"; // green snake
					break;
				case Ids.FRUIT:
					ctx.fillStyle = "#f00"; // red fruit
					break;
			}
			// fill rect takes start coords and width and height of rectangle
			ctx.fillRect(x*tile_width, y*tile_height, tile_width, tile_height);
		}
	}
	ctx.font = '12pt Arial';
    ctx.fillStyle = '#fff'; // white
    ctx.textBaseline = 'top';
    ctx.fillText("Score: " + score, 7, 7);
    ctx.fill();
    ctx.fillStyle = "#000"; // black background

    if (currentState === states.pause) {
    	ctx.font = '24pt Arial';
        ctx.fillStyle = '#fff'; // white
        ctx.textBaseline = 'top';
        ctx.fillText("PAUSED", canvas.width/2 - 60, canvas.height/2 - 35);
        ctx.font = '10pt Arial';
        ctx.fillText("Hit SPACE to unpause", canvas.width/2 - 61, canvas.height/2);
        ctx.fill();
        ctx.fillStyle = "#000"; // black background
    }

    if (currentState === states.death) {
    	ctx.font = '24pt Arial';
        ctx.fillStyle = '#fff'; // white
        ctx.textBaseline = 'top';
        ctx.fillText("GAME OVER", canvas.width/2 - 100, canvas.height/2 - 35);
        ctx.font = '10pt Arial';
        ctx.fillText("Hit SPACE to continue", canvas.width/2 - 70, canvas.height/2);
        ctx.fill();
        ctx.fillStyle = "#000"; // black background
    }
}

function update() {
	frames++;

	if (currentState !== states.pause && currentState !== states.death) {
		if (keystate[KEY_LEFT] && Snake.direction !== Direction.RIGHT)
			Snake.direction = Direction.LEFT;

		if (keystate[KEY_RIGHT] && Snake.direction !== Direction.LEFT)
			Snake.direction = Direction.RIGHT;

		if (keystate[KEY_UP] && Snake.direction !== Direction.DOWN)
			Snake.direction = Direction.UP;

		if (keystate[KEY_DOWN] && Snake.direction !== Direction.UP)
			Snake.direction = Direction.DOWN;


		if (frames % speed === 0) {  // change the divisor to speed up or slow down the snake
			var new_x = Snake.last.x;
			var new_y = Snake.last.y;

			switch (Snake.direction) {
				case Direction.LEFT:
					new_x--;
					break;
				case Direction.RIGHT:
					new_x++;
					break;
				case Direction.UP:
					new_y--;
					break;
				case Direction.DOWN:
					new_y++; // down is positive y as origin is at top left
					break;
			}

			// 0 to grid width is the size of the canvas.

			if (new_x < 0) {
				new_x = Grid.width - 1;
			} else if (new_x > Grid.width - 1) {
				new_x = 0;
			} else if (new_y < 0) {
				new_y = Grid.height - 1;
			} else if (new_y > Grid.height - 1) {
				new_y = 0;
			} else if (Grid.get(new_x, new_y) === Ids.SNAKE) {
				// -1 as the snake is 1 tile wide and initially 1 tile long
				return currentState = states.death; // return to break out of the function.
			}

			// every 5 frames the head is removed and it becomes the tail, this
			// is repeated infinately, basically the snake is slowly being re
			var tail;

			// if you eat the fruit:
			if (Grid.get(new_x, new_y) === Ids.FRUIT) {
				tail = {x: new_x, y : new_y}; // new tail is the position of the fruit.
				genFood();
				score++;
			} else {
				tail = Snake.remove();
				Grid.set(Ids.EMPTY, tail.x, tail.y);
				tail.x = new_x;
				tail.y = new_y;
			}

			Grid.set(Ids.SNAKE, tail.x, tail.y);
			Snake.insert(tail.x, tail.y); // add the tail to the snake queue
		}

		if (score >= 31) {
		  speed = 1;
		}	else if (score <= 30 && score >= 26) {
			speed = 2;
		} else if (score <= 25 && score >= 21) {
			speed = 3;
		} else if (score <= 20 && score >= 16) {
			speed = 3;
		} else if (score <= 15 && score >= 11) {
			speed = 4;
		} else if (score >= 5 && score <= 10) {
			speed = 6;
		}
	}
}

main();
