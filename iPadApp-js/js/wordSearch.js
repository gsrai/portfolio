var letters = [
    ["B", "A", "L", "C", "O", "N", "Y", "F", "Q", "W", "E", "R", "T", "Y", "U"],
    ["I", "O", "I", "P", "A", "S", "D", "F", "L", "G", "H", "J", "K", "L", "Z"],
    ["X", "C", "G", "V", "B", "N", "M", "Q", "W", "O", "E", "R", "T", "Y", "U"],
    ["I", "O", "H", "P", "A", "S", "D", "F", "G", "H", "O", "P", "J", "K", "L"],
    ["Z", "X", "T", "C", "S", "V", "B", "N", "M", "Q", "W", "R", "E", "R", "T"],
    ["Y", "U", "S", "I", "O", "T", "P", "A", "S", "D", "F", "O", "G", "H", "J"],
    ["K", "D", "E", "C", "O", "R", "A", "T", "I", "O", "N", "J", "K", "L", "Z"],
    ["X", "C", "A", "V", "B", "N", "R", "G", "M", "Q", "W", "E", "E", "R", "T"],
    ["Y", "U", "T", "I", "O", "P", "A", "A", "E", "S", "D", "C", "F", "G", "H"],
    ["H", "J", "I", "L", "Z", "X", "I", "C", "V", "B", "N", "T", "M", "Q", "W"],
    ["E", "R", "N", "T", "Y", "U", "L", "I", "O", "P", "A", "I", "S", "D", "F"],
    ["O", "R", "G", "A", "N", "P", "I", "P", "E", "S", "G", "O", "H", "J", "K"],
    ["P", "R", "O", "S", "C", "E", "N", "I", "U", "M", "L", "N", "Z", "X", "C"],
    ["V", "B", "N", "M", "Q", "W", "G", "E", "R", "T", "Y", "U", "I", "O", "P"],
    ["A", "S", "D", "F", "G", "H", "S", "J", "K", "L", "Z", "X", "C", "V", "B"]
];

var selected = [];
var selectedPos = [];
var unlockCount = 0;

// dont put currently selected into selected until its been verified that it is adjacent to a selected letter
function adjacentLetter(x, y) {
	if (selected.length === 0) return true;

	if (withinBounds(x + 1, y) && nextToSelected(x + 1, y)) return true; // right
	if (withinBounds(x - 1, y) && nextToSelected(x - 1, y)) return true; // left
	if (withinBounds(x, y + 1) && nextToSelected(x, y + 1)) return true; // down
	if (withinBounds(x, y - 1) && nextToSelected(x, y - 1)) return true; // up

	if (withinBounds(x - 1, y - 1) && nextToSelected(x - 1, y - 1)) return true; // top left
	if (withinBounds(x + 1, y - 1) && nextToSelected(x + 1, y - 1)) return true; // top right
	if (withinBounds(x - 1, y + 1) && nextToSelected(x - 1, y + 1)) return true; // bottom left
	if (withinBounds(x + 1, y + 1) && nextToSelected(x + 1, y + 1)) return true; // bottom right

	return false;
}

// checks if position is within bounds, dont want to check outside of array bounds and get an exception
function withinBounds(x, y) {
	return (x >= 0 && x < 15) && (y >= 0 && y < 15);
}

// traverses the selected position array to see if the current position matches with any other.
function nextToSelected(x, y) {
	for (var i = 0; i < selectedPos.length; i++) {
		if (selectedPos[i].x === x && selectedPos[i].y === y)
			return true;
	};
	return false;
}

// resets the selected letters, when the user clicks on a letter that is not adjacent to a selected letter
function resetSelected() {
	for (var i = 0; i < selectedPos.length; i++) {
		$("#letterR" + (selectedPos[i].y) + "C" + (selectedPos[i].x)).removeClass("selectedLetter");
	};
	selected = [];
	selectedPos = [];
}

function isValidSelection(x, y) {
	if (selected.length < 2) return true; // if there is only one or no selections then return valid is true.

	if (isTrajectoryDown()) {
		if (predictedDown(x, y)) return true; // if the current selection is the same as the predicted then it is valid
		return false;
	}
	
	if (isTrajectoryRight()) {
		if (predictedRight(x, y)) return true;
		return false;
	}
	
	if (isTrajectoryBottomRight()) {
		if (predictedBottomRight(x, y)) return true;
		return false;
	}
	
	return false;
}

function predictedDown(x, y) {
	// only need to check the direction of the first two
	var down = false;

	if (selectedPos[0].x === x && selectedPos[1].x === x) down = true; // can try is y greater than previous y (as going down is y + 1)

	console.log("down is " + down);
	return down;
}

function predictedRight(x, y) {
	// only need to check the direction of the first two
	var right = false;

	if (selectedPos[0].y === y && selectedPos[1].y === y) right = true;

	console.log("right is " + right);
	return right;
}

function predictedBottomRight(x, y) {
	// only need to check the direction of the last two. as the new selection has to be greater than both of them

	var bottomRight = false;
	var end = selectedPos.length;

	if (selectedPos[end - 2].x < x && selectedPos[end - 1].x < x && selectedPos[end - 2].y < y && selectedPos[end - 1].y < y) bottomRight = true;


	console.log("bottomRight is " + bottomRight);
	return bottomRight;
}

function isTrajectoryDown() {
	// checks if the selection is downward
	if (selectedPos[0].x === selectedPos[1].x) return true;

	return false;
}

function isTrajectoryRight() {
	// checks if the selection is downward
	if (selectedPos[0].y === selectedPos[1].y) return true;

	return false;
}

function isTrajectoryBottomRight() {
	// checks if the selection is downward
	if (selectedPos[0].x < selectedPos[1].x && selectedPos[0].y < selectedPos[1].y) return true;

	return false;
}

function setAsFound() {
	for (var i = 0; i < selectedPos.length; i++) {
		$("#letterR" + (selectedPos[i].y) + "C" + (selectedPos[i].x)).removeClass("selectedLetter");
		$("#letterR" + (selectedPos[i].y) + "C" + (selectedPos[i].x)).addClass("wordFound");
	};
	selected = [];
	selectedPos = [];
}

function foundWord() {
	var word = selected.join("");

	if (word === "BALCONY") {
		setAsFound();
		$("#wordBALCONY").css("text-decoration", "line-through");
		$('#viewBlacony').click();
		unlockCount++;
	}
	
	if (word === "DECORATION") {
		setAsFound();
		$("#wordDECORATION").css("text-decoration", "line-through");
		$('#viewDecoration').click();
		unlockCount++;
	}

	if (word === "FLOOR") {
		setAsFound();
		$("#wordFLOOR").css("text-decoration", "line-through");
		$('#viewFloor').click();
		unlockCount++;
	}

	if (word === "LIGHT") {
		setAsFound();
		$("#wordLIGHT").css("text-decoration", "line-through");
		$('#viewLight').click();
	}

	if (word === "ORGANPIPES") {
		setAsFound();
		$("#wordORGANPIPES").css("text-decoration", "line-through");
		$('#viewOrganpipes').click();
	}

	if (word === "PROJECTION") {
		setAsFound();
		$("#wordPROJECTION").css("text-decoration", "line-through");
		$('#viewProjection').click();
	}

	if (word === "PROSCENIUM") {
		setAsFound();
		$("#wordPROSCENIUM").css("text-decoration", "line-through");
		$('#viewProscenium').click();
	}

	if (word === "RAILINGS") {
		setAsFound();
		$("#wordRAILINGS").css("text-decoration", "line-through");
		$('#viewRailings').click();
	}

	if (word === "SEATING") {
		setAsFound();
		$("#wordSEATING").css("text-decoration", "line-through");
		$('#viewSeating').click();
	}

	if (word === "STAGE") {
		setAsFound();
		$("#wordSTAGE").css("text-decoration", "line-through");	
		$('#viewStage').click();
	}

	if (unlockCount >= 1) {
		$("#POIbuttonWS").unbind('click');
		$("#POIbuttonWS").click(function(e) {
			console.log("POI button is unlocked");
		});
	}
}

function draw() {
	// drawing of the word search
	for (var i = 0; i < letters.length; i++) {
		for (var j = 0; j < letters.length; j++) {
			var myDiv = $("<div></div>", {id:"letterR" + (i) + "C" + (j), width: "32", height: "30"});

		// the id of each div used R for rows and C for columns as name delimiters, stops id clashing
			myDiv.addClass("letter"); // css styling
			myDiv.addClass("normalLetter"); // css styling

			myDiv.click(function(letter, ii, jj){
				return function(){
					console.log(letter + " is adjacent? " +adjacentLetter(jj, ii));
					// if there are more than two in a row the new selection has to be on the same plane
					if (adjacentLetter(jj, ii) && isValidSelection(jj, ii)) {
						$("#letterR" + (ii) + "C" + (jj)).addClass("selectedLetter");
						selected.push(letter);
						selectedPos.push({x:jj, y:ii});
						console.log(selected.join(""));
						foundWord();
					} else {
						resetSelected();
						console.log(selected.join(""));
					}
				}		
			}(letters[i][j], i, j));

			myDiv.html(letters[i][j]);
			$("#GameWidget").append(myDiv); // append the seat div to the dom
			//document.getElementById('letter' + (i) + "" + (j)).innerHTML = "" + letters[i][j]; // draw the seat number on the div

			if((j + 1) === 15) $("#GameWidget").append("<br>"); // new line! (in drawing the seats), 10 seats per row
		}
	};
}

$(document).ready(function() {
	draw();
	//alert("Don't want to play but still curious?\nFind TWO words to unlock the skip game\nfeature and view all artefacts.");

	$("#HelpWS").click(function(){
		alert("Don't want to play but still curious?\nFind TWO words to unlock the skip game\nfeature and view all artefacts.\nYou must select letters in order to find a word.");
	});

	$("#POIbuttonWS").click(function(e) {
		e.preventDefault();
		console.log("POI button is locked");
	});
});

