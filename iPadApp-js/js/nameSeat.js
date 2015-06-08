var seats = [];
var selected = null;

// initalise seat array
function initSeats(seats, number) {
	var seatCount = 1;
	for (var i = 0; i < number; i++) {
		seats[i] = {
			name : "",
			number : seatCount++,
			available : true,
			type : "none"
		};
	};

	return seats;
}

function selectSeat(selectedSeat, previousSeat) {
	previousSeat.removeClass("seatSelected");
	selectedSeat.addClass("seatSelected");
	return selectedSeat.selector.substring(5); // selectedSeat is a dom element not seat object
}

function clearSelected() {
	if(selected !== null) {
		$("#seat" + selected).removeClass("seatSelected");
		selected = null;
		$("#Info").html("");	
	}
}

function main() {

	seats = initSeats(seats, 70); // initialse 70 seat objects


	seats[6].available = false;
	seats[6].name = "David Beckham";
	seats[6].type = "director";

	seats[7].available = false;
	seats[7].name = "Victoria Beckham";
	seats[7].type = "director";

	seats[54].available = false;
	seats[54].name = "David Cameron";
	seats[54].type = "director";

	seats[39].available = false;
	seats[39].name = "Michael Caine";
	seats[39].type = "director";

	seats[24].available = false;
	seats[24].name = "Quentin Tarantino";
	seats[24].type = "director";	

	seats[41].available = false;
	seats[41].name = "Gagondeep Srai";
	seats[41].type = "standard";

	seats[3].available = false;
	seats[3].name = "Devesh Rathod";
	seats[3].type = "standard";

	seats[27].available = false;
	seats[27].name = "Abdifatah Osman";
	seats[27].type = "standard";

	seats[53].available = false;
	seats[53].name = "Hussam Abdulhamid";
	seats[53].type = "standard";

	// drawing of seats
	for (var i = 0; i < seats.length; i++) { // i is always 70 when events are triggered
		var myDiv = $("<div></div>", {id:"seat" + (i+1), width: "32", height: "30"});

		if (seats[i].available) {
			myDiv.addClass("seat"); // css styling
			myDiv.addClass("seatAvailable"); // css styling
		} else {
			myDiv.addClass("seat"); // css styling
			myDiv.addClass("seatUnavailable"); // css styling
		}

		if (seats[i].available) {
			myDiv.click(function(s){
				return function(){
					var option = confirm("seat " + s.number + " is available!\nWould you like to book this seat?");
					if(option) {
						if(selected === null) {
							$("#seat" + s.number).addClass("seatSelected");
							selected = s.number;
						} else {
							selected = selectSeat($("#seat" + s.number), $("#seat" + selected));
						}
						$("#Info").html("Seat Selected: " + selected);
					}		
				};
			}(seats[i]));
		} else {
			myDiv.click(function(s){
				return function(){
					alert("seat " + s.number + " unavailable!\nName on Seat: " + s.name + "\nSeat Type: " + s.type);
					//$("#Info").html("Seat Selected: " + selected); // when a named seat is selected
				};
			}(seats[i])); // closures need to be called to save the stack frame.
		}

		$("#seatWidget").append(myDiv); // append the seat div to the dom
		document.getElementById('seat' + (i + 1)).innerHTML = "" + (i + 1); // draw the seat number on the div

		if((i + 1) % 10 === 0) $("#seatWidget").append("<br>"); // new line! (in drawing the seats), 10 seats per row
	};

	// clears the selected seat
	$("#ClearSel").click(function(){
		clearSelected();
	});

}

$(document).ready(function() {
	if(document.getElementById('seatWidget') !== null) {

		main();

		$("#Help").click(function(){
			alert("You can name a seat for \u00A3750, \nor choose one of our exclusive Director's seats for \u00A31500. \nYour gift will go directly towards the cost of restoration and \nwill help secure the future of this historic venue.");
		});

		var handler = StripeCheckout.configure({
        	key: 'pk_test_cp21BcECf4kMMUbSlRlZlsMo',
	        token: function(token) {
	          	// Use the token to create the charge with a server-side script.
	          	// You can access the token ID with `token.id`

	          	//This will be printed when the transaction is successful. To charge, server side scripting is required.
	          	if(token.id){
					var seatname = $("#nameOfSeat").val();
					var seatType = $("#SeatType").val();
					console.log($("#SeatType").val());
					console.log(seatType);
					// selected - 1 as seats[] start from 0 not 1
					seats[selected - 1].available = false;
					seats[selected - 1].name = seatname;
					seats[selected - 1].type = seatType;
					$("#seat" + selected).addClass("seatUnavailable"); // css styling
					$("#seat" + selected).removeClass("seatAvailable");
					$("#seat" + selected).unbind();
					$("#seat" + selected).click(function(s){
						return function(){
							alert("seat " + s.number + " unavailable!\nName on Seat: " + s.name + "\nSeat Type: " + s.type);
						};
					}(seats[selected - 1]));
					clearSelected();
					$("#nameOfSeat").val("");
					$("#emailNameSeat").val("");

	            	document.location.href = "#NameSeat3";	
	          	}
	        }
      	});

		// donate button logic
		$("#seatPayButton").click(function(s){
			return function() {
				if(selected !== null && $("#nameOfSeat").val() !== "" && $("#emailNameSeat").val() !== "") {
					var price = 75000;
					var des = "1 Seat (GBP750.00)";
					if($("#SeatType").val() === "director") {
						price = 150000;
						des = "1 Director Seat (GBP1500.00)";
					}
					// Open Checkout with further options
		        	handler.open({
		          		name: 'Name a Seat',
		          		currency: 'gbp',
		          		description: des,
		          		amount: price
		        	});	
				} else {
					alert("Please select a seat, enter a name and enter your email, thank you.");
				}
			};
		}(seats));

      	// Close Checkout on page navigation
      	$(window).on('popstate', function() {
        	handler.close();
      	});

		// resets the book a seat widgit
		$("#BackBtn").click(function(){
			$("#seat" + selected).removeClass("seatSelected");
			$("#nameOfSeat").val("");
			$("#emailNameSeat").val("");
		});

	}
});

