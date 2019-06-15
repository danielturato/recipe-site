$('#addIngredient').click(function () {
    var ingredientCount = $(".ingredient-row").length;
    var div = '<div class="ingredient-row">';
    div = div + '<div class="prefix-20 grid-30">';
    div = div + '<p><input name="ingredients[' + ingredientCount + '].name"></p></div>';
    div = div + '<div class="grid-30">';
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