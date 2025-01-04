
const toggleSidebar = () => {
	if ($(".sidebar").is(":visible")) {
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
	}
	else {
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
	}
};

const searchContact = () => {
	let query = $("#search-input").val();

	if (query == '') {
				$(".search-result").html("Type Something..");
				$(".search-result").show();
	}
	else {
		//send req to server
		let url = `http://localhost:8282/search/${query}`;

		fetch(url)
			.then((response) => {
				return response.json();
			})
			.then((data) => {
				console.log(data);
				let text = `<div class='list-group'>`;
				data.forEach((contact) => {
					text += `<a href='/user/view-contactDetail/${contact.con_id}' class='list-group-item list-group-item-action'> <img src="/image/${contact.con_imgUrl}" alt=""
								class="search_profile_picture" /> <span style='font-size: 17px; font-weight: bold'>${contact.con_name}</span> <span style='font-size: 12px'>(${contact.con_nickName})</span></a>`;
				});
				text += `</div>`;

				$(".search-result").html(text);
				$(".search-result").show();
				
				if (data == '') {
				$(".search-result").html("No Contacts Found!");
				$(".search-result").show();
	}
			});

	}
	
	
}
var mouse_is_inside = false;
 $("body").mouseup(function(){ 
        if(! mouse_is_inside) $('.search-result').hide();
    });

