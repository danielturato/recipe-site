$('#addIngredient').click(function () {
    var ingredientCount = $(".ingredient-row").length;
    var div = '<div class="ingredient-row"><div class="prefix-20 grid-30">';
    div = div + '<p><input name="ingredients[' + ingredientCount + '].name"></p></div><div class="grid-30">';
    div = div + '<p><input name="ingredients[' + ingredientCount + '].condition"></p></div>';
    div = div + '<div class="grid-10 suffix-10"><p><input name="ingredients[';
    div = div + ingredientCount + '].quantity"></p></div></div>';
    $("#addIngredientDiv").before(div);
});

$('#newStep').click(function () {
    var stepsCount = $(".step-row").length;
    var div = '<div class="step-row"><div class="prefix-20 grid-80"><p><input ';
    div = div + 'name="steps[' + stepsCount +']"/></p></div></div>';
    $("#newStepDiv").before(div);
});

$('#editPhoto').click(function () {

    var val = $(this).val();

    if(val === "notEdited") {
        $('#recipePhoto').hide();
        $(this).text("Cancel");
        $(this).val("edited");
        $('#photoInput').attr("type", "file");
    } else {
        $('#recipePhoto').show();
        $(this).text("Change recipe photo");
        $(this).val("notEdited");
        $('#photoInput').attr("type", "hidden");
    }

});

$("[id ^= 'deleteButton']").each(function () {
    $(this).click(function () {
        var result = confirm("Are you sure you want to delete this recipe?");
        var id = $(this).attr('id').substr(12);
        if (result) {
            var form = '#deleteForm' + id;
            $(form).submit();
        }
    })
});

$("#signUpButton").click(function () {
   var passwordOne = $("#passwordOne").val();
   var passwordTwo = $("#passwordTwo").val();

   if (passwordOne === passwordTwo) {
       $("#signUpForm").submit();
   } else {
       alert("The passwords don't match!");
   }

});

$('#favButton').click(function () {
   $('#favButtonForm').submit();
});

$('#flashMessage').slideDown(function () {
   setTimeout(function () {
       $('#flashMessage').slideUp();
   }, 5000);
});

function recipeSearchQuery(elem) {
    if (elem.attr("id") === 'recipeSearch') {
        if (elem.keyCode === 13) {
            $('#queryForm').submit();
        }
    } else {
        $('#queryForm').submit();
    }

}