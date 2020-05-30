<!DOCTYPE html>
<html>

<head>
	<title>search</title>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" href="search.css">
	<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
	<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js" integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI" crossorigin="anonymous"></script>
</head>

<body>
	<div class="back">
		<form action="../index.html" method="post">
			<button type="submit" class="btn btn-primary">Back</button>
		</form>
	</div>

	<?php
		include ("worker.php");

		$worker = new Worker();

		// If from index.html, get rec
		$query = array();
		$query["PrimaryAttribute"] = $_POST["PrimaryAttribute"];
		$query["Fraction"] = $_POST["Fraction"];
		$query["HeroType"] = $_POST["HeroType"];
		$query["Complexity"] = $_POST["Complexity"];
		$query["PlayerName"] = $_POST["PlayerName"];
		if ($query["PrimaryAttribute"] != null &&
			$query["Fraction"] != null &&
			$query["HeroType"] != null &&
			$query["Complexity"] != null &&
			$query["PlayerName"] != null) 
		{
				$worker->setQuery($query);
				$worker->curlLambda("GetRec");
		}

		// Generate rec table
		$worker->curlLambda("GetView");
		$recTable = $worker->generateRecTable();
		
		// If from search.php itself, put/delete cache
		$addHero = $_GET["AddHero"];
		$deleteHero = $_GET["DeleteHero"];
		if ($addHero != null ) {
			$worker->addCache($addHero);
		}
		if ($deleteHero != null ) {
			$worker->deleteCache($deleteHero);
		}

		// Generate cache table
		$worker->curlLambda("GetCache");
		$cacheTable = $worker->generateCacheTable();		

		echo $recTable;
		echo $cacheTable;
	?>
</body>

</html>
