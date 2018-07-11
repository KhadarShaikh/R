resourceApp.filter('ifEmpty', function() {
	return function(input,undefined) {
		if (input === '') {
			return 'undefined';
		}
		return input;
	}
});
