const source = document.getElementById("source");
const target = document.getElementById("target");
const select_One = document.getElementById("select_One");
const select_Two = document.getElementById("select_Two");
const form = document.getElementById("form");

$(document).on("submit", form, function (e) {
  e.preventDefault();

  //validate the user inputs
  if (source.value == "") {
    alert("Please enter amount");
  } else if (select_One.value == select_Two.value) {
    alert("Please select a different Currency for the one side");
  } else if (
    select_One.value == "Choose a Currency" ||
    select_Two.value == "Choose a Currency"
  ) {
    alert("Please select a Currency");
  } else {
    //send user input data to the main.py
    $.ajax({
      type: "POST",
      url: "/convert",
      data: {
        sourceValue: $(source).val(),
        sourceCountry: $(select_One).val(),
        targetCountry: $(select_Two).val(),
      },
      success: function () {
        console.log($(source).val());
        console.log($(select_One).val());
        console.log($(select_Two).val());
      },
      error: function (err) {
        console.log(err.message);
      },
    }).done(function (data) {
      $(target).val(data.target_value);
    });
  }
});
