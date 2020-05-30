<!DOCTYPE html>
<html>

<head>
	<title>search</title>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
	
	<?php
		$heroName = $_GET["HeroName"];
		$css = "";

		if ($heroName != null) {
			$heroNameNoSpace = str_replace(" ", "", $heroName);
			$css = <<<EOF
				<link rel="stylesheet" type="text/css" href="$heroNameNoSpace.css">
			EOF;
		} else {
			$css = <<<EOF
				<link rel="stylesheet" type="text/css" href="background.css">
			EOF;
		}

		echo $css;
	?>

	<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
	<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js" integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI" crossorigin="anonymous"></script>
</head>

<body>
	<div class="back">
		<form action="search.php" method="post">
			<button type="submit" class="btn btn-primary">Back</button>
		</form>
	</div>

	<?php
		include ("worker.php");
		
		$heroName = $_GET["HeroName"];
		$coreAbility = $_GET["CoreAbility"];
		$coreItem = $_GET["CoreItem"];
		$playerName = $_GET["PlayerName"];

		$query = array();
		$worker = new Worker();

		if ($heroName != null) {
			$query["HeroName"] = $heroName;
			$worker->setQuery($query);
			$worker->curlLambda("GetHero");
		}
		if ($coreAbility != null) {
			$query["AbilityName"] = $coreAbility;
			$worker->setQuery($query);
			$worker->curlLambda("GetAbility");
		}
		if ($coreItem != null) {
			$query["ItemName"] = $coreItem;
			$worker->setQuery($query);
			$worker->curlLambda("GetItem");
		}
		if ($playerName != null) {
			$query["PlayerName"] = $playerName;
			$worker->setQuery($query);
			$worker->curlLambda("GetPlayer");
		}

		$desTable = $worker->generateDesTable();
		echo $desTable;
	?>
</body>

</html>
