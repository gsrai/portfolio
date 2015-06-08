/*
 *	Mobile UX Main javascript file for the mobile app
 * 	Gagondeep Srai
 */

/*
 *	An event listener for the menu button, which toggles the reveal of the navigation menu.
 * 	Initially make the navigation menu invisible by setting navigation_visible to false.
 *  Then look for the menubutton and add a click event listener to it. If the navigation is invisible then
 *  reveal the menu (as a BLOCK element (display: block, inline or none)) and set the navigation as
 *  visible so that in the next event this block of code fails.
 *
 *  When the first if statement/block fails then the else block will execute (when the menu is visible).
 */
//
var navigation_visible = false;

$(".menuButton").click(function() {
	toggleNav();
});

function toggleNav() {
	if (!navigation_visible) {
		//document.getElementById('navMenu').style.display = "block";
		document.getElementById('navMenuHome').style.display = "block";
		document.getElementById('navMenuOC').style.display = "block";
		document.getElementById('navMenuNAS1').style.display = "block";
		document.getElementById('navMenuNAS2').style.display = "block";
		document.getElementById('navMenuNAS3').style.display = "block";
		document.getElementById('navMenuPOI').style.display = "block";
		document.getElementById('navMenuCUS').style.display = "block";
		document.getElementById('navMenuFUS').style.display = "block";
		document.getElementById('navMenuEvents1').style.display = "block";
		document.getElementById('navMenuEvents2').style.display = "block"; 
		document.getElementById('navMenuWord').style.display = "block";
		document.getElementById('navMenuConfirmationEvent').style.display = "block";
		navigation_visible = true;
	} else {
		//document.getElementById('navMenu').style.display = "none";
		document.getElementById('navMenuHome').style.display = "none";
		document.getElementById('navMenuOC').style.display = "none";
		document.getElementById('navMenuNAS1').style.display = "none";
		document.getElementById('navMenuNAS2').style.display = "none";
		document.getElementById('navMenuNAS3').style.display = "none";
		document.getElementById('navMenuPOI').style.display = "none";
		document.getElementById('navMenuCUS').style.display = "none";
		document.getElementById('navMenuFUS').style.display = "none";
		document.getElementById('navMenuEvents1').style.display = "none";
		document.getElementById('navMenuEvents2').style.display = "none";
		document.getElementById('navMenuWord').style.display = "none";
		document.getElementById('navMenuConfirmationEvent').style.display = "none";
		navigation_visible = false;
	}
}

//change navigation_visible to false and invoke menu button clcick

function hideNavMenu() {
	navigation_visible = true;
	toggleNav();
}

/*
 *	On the homepage there is a button/link which reveals more text. homepageParagraph is an object.
 * 	It stores the variables:
 *  showMore - show more text yes or no? operates like navigation_visible.
 *  lessText - is the text displayed when the user wants to unreveal the more text.
 *  moreText - is the text displayed when the user wants to reveal more text.
 *
 *  why did i use an object? to preserve the global namespace. In our designs other pages use this
 *  "reveal more text" functionality. If you have this functionality, then copy and paste the object
 *  and change the name to <page name>Paragraph or whatever is more semantic.
 */
var homepageParagraph = {
	showMore : false,
	lessText : "",
	moreText : "The University of Westminster film graduates occupy significant roles within the industry, creating outstanding films and cementing our world class reputation. The restoration of the Regent Street Cinema will reinforce our position as a leading global centre for excellence in the arts and film production. The cinema will offer the best repertory programming and be a place to see quality world cinema. In addition, bringing together students and industry professionals will nurture future talent and provide a much-needed platform for our outstanding film students to showcase their work. We are working with Tim Ronalds Architects, a practice specialising in arts, education and public projects who have won multiple awards for their restoration of the Hackney Empire. Thanks to generous contributions from the Quintin Hogg Trust, the Heritage Lottery Fund and the Garfield Weston Foundation we have already raised over two thirds of the &pound;6.1million required to restore and reopen the Cinema. Now you can be involved in bringing the project to completion and the Cinema to life."
};

/*
 *	Event listener for the "more..." button/link. Basically it inserts the text into the span element
 *  inside the paragraph element and changes the hyperlink anchor text to 'less' or 'more' depending on
 *  the showMore variable.
 *
 *  Need to check for null incase called on another page
 */
if(document.getElementById('homepageParagraphButton') !== null) {

	document.getElementById('homepageParagraphButton').addEventListener("click", function() {

		if (!homepageParagraph.showMore) {
			document.getElementById('homepageSubParagraph1').innerHTML = homepageParagraph.moreText;
			document.getElementById('homepageParagraphButton').innerHTML = "less...";
			homepageParagraph.showMore = true;
		} else {
			document.getElementById('homepageSubParagraph1').innerHTML = homepageParagraph.lessText;
			document.getElementById('homepageParagraphButton').innerHTML = "more...";
			homepageParagraph.showMore = false;
		}

	});
}

/*
 *	Event listener for the Google maps go button.
 *
 *  Need to check for null incase called on another page
 */
if(document.getElementById('go') !== null) {

	document.getElementById('go').addEventListener("click", function() {
		var postcode = document.getElementById('clInput').value;
		if (postcode !== "") {
			var	res = postcode.split(" ");
			document.getElementById('map').src = "https://www.google.com/maps/embed/v1/directions?origin=University+Of+Westminster,+Regent+Street,+London,+United+Kingdom&destination=" + res[0] + "+" + res[1] + ",+United+Kingdom&key=AIzaSyDpEg5_-DhNCqFGMbyYUaJEZKuZtKkzwn8";
		}
	});

}

/*
 *	slider widget, uses an array of objects to hold the slide date and text. Then I animate the widget
 *  via a click event.
 */

var sliderParagraph = [];

// slide 0 doesn't exist
sliderParagraph.push({
 	date : "",
	text : ""
});


// slide 1
sliderParagraph.push({
 	date : "August 6, 1838", 
	text : "The Polytechnic Institute opens at 309 Regent Street."
});


sliderParagraph.push({
 	date : "March 23, 1841", 
	text : "Europe's first photographic studio open's on the roof of the Polytechnic building. Charles Dickens is one of its earliest visitors."
});

sliderParagraph.push({
	date : "January 1, 1848",
	text : "A theatre is opened in the Regent Street building and soon becomes famous for its pioneering magic lantern shows."
});

sliderParagraph.push({
 	date : "February 21, 1896",
	text : "The Lumiere brothers' Cinematographe shows moving image to a paying British audience for the first time, paving the way for a century of cinematographic innovation."
});

sliderParagraph.push({
 	date : "November 3, 1900",
	text : "The Cinema's first advert appears in the Times using the name 'Polytechnic Theatre'."
});

sliderParagraph.push({
 	date : "January 1, 1926",
	text : "Art deco alteration works to the Cinema provide the art deco white wall interior with areas of gilded embossed plaster details that we see today."
});

sliderParagraph.push({
 	date : "January 1, 1936",
	text : "A Compton Organ is installed to accompany the stars of the silent screen. Recently restored, the Organ is in full working order today and plans are underway to make the organ's pipework and percussion visible from the exterior."
});

sliderParagraph.push({
 	date : "May 1, 1963",
	text : "The Cinema was a popular venue for film premieres. In the 1960's Roman Polanski's Cul De Sac and Oscar nominated Kwaidan both premiered there."
});

sliderParagraph.push({
 	date : "January 1, 1970",
	text : "The Polytechnic of Central London is formed as one of the 30 new polytechnics and a year later offers the UK's first Honours degree course in film."
});

sliderParagraph.push({
 	date : "April 1, 1980",
	text : "The Cinema closes, Classic Cinemas Ltd vacates the building and the space is no longer used as a commercial Cinema."
});

sliderParagraph.push({
 	date : "June 1, 1995",
	text : "The Cinema auditorium is refurbished. The last refurbishment to take place on the auditorium was in 1995."
});

sliderParagraph.push({
 	date : "February 21, 1996",
	text : "A centenary anniversary of the first Lumiere performance is held at the Cinema as part of a special four day Lumiere Festival."
});

sliderParagraph.push({
 	date : "September 1, 2010",
	text : "The Qunitin Hogg Trust donates \u00A31m to the University's campaing to restore and reopen the Cinema."
});

sliderParagraph.push({
 	date : "January 1, 2012",
	text : "Thanks to a \u00A31.5m Heritage Lottery Fund grant the project is two thirds of the way to meeting its \u00A36.1m target."
});

sliderParagraph.push({
 	date : "June 21, 2012",
	text : "University of Westminster students win a prestigious Student Academy Award for their film For Elsie."
});

sliderParagraph.push({
 	date : "November 29, 2012",
	text : "James Bond screenwriter and University of Westminster alumnus Neal Purvis and his writing partner Robert Wade host a special Q&A event in the Regent Street Cinema."
});

sliderParagraph.push({
 	date : "February 21, 2014",
	text : "The restoration works are set to go ahead as plans are given the green light by Westminster City Council."
});

sliderParagraph.push({
 	date : "March 1, 2014",
	text : "The Cinema supports Westminster Film Forum, the University's first student led film festival. The winning prizes include one year Cinema membership."
});

sliderParagraph.push({
 	date : "April 21, 2014 ",
	text : "The Cinema becomes a hard hat zone as building works commence to restore the site to its former grandeur."
});

 function switchParagraph(slide) {
 	$('#sliderTitle').text(sliderParagraph[slide].date);
 	$('#sliderText').text(sliderParagraph[slide].text);
 }

$(document).ready(function() {
	if(document.getElementById('SliderWidget') !== null) {
		var currentSlide = 1;

		$("#sliderBtnRight").click(function() {
			if (currentSlide !== 19) {
				$("#SliderWidget .slides").animate({'margin-left' : '-=600'}, 500, function() {
					currentSlide++;
					switchParagraph(currentSlide);
				});
			}
		});

		$("#sliderBtnLeft").click(function() {
			if (currentSlide !== 1) {
				$("#SliderWidget .slides").animate({'margin-left' : '+=600'}, 500, function() {
					currentSlide--;
					switchParagraph(currentSlide);
				});
			};	
		});

		/*
		 * swipe gestures using jquery, to navigate the timeline
		 */
		$("#SliderWidget .slides").on("swipeleft" ,function() {
			if (currentSlide !== 19) {
				$("#SliderWidget .slides").animate({'margin-left' : '-=600'}, 500, function() {
					currentSlide++;
					switchParagraph(currentSlide);
				});
			}
		});

		$("#SliderWidget .slides").on("swiperight" ,function() {
			if (currentSlide !== 1) {
				$("#SliderWidget .slides").animate({'margin-left' : '+=600'}, 500, function() {
					currentSlide--;
					switchParagraph(currentSlide);
				});
			};	
		});
	}
});


// homepage slider code
var currentHomePageSlide = 1;

setInterval(function() {
	$("#homepageSlider .slides").animate({'margin-left' : '-=315px'}, 1000, function() {
		currentHomePageSlide++;
		if (currentHomePageSlide === 4) {
			currentHomePageSlide = 1;
			$("#homepageSlider .slides").css('margin-left', 0);
		}
	});
}, 3000);