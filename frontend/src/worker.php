<?php
	// 
	// Worker for recommendation/cache/description tables
	//

	class Worker {
		private $query;
		private $rec;
		private $cache;
		private $des;

		function __construct($query=null, $rec=null, $cache=null, $des=null) {
			if ($query == null) {
				$this->query = array();
			} else {
				$this->query = $query;
			}
			
			$this->rec = $rec;
			$this->cache = $cache;
			$this->des = $des;
		}

		//
		// Setter for query
		//

		public function setQuery($query) {
			$this->query = $query;
		}

		//
		// Curl AWS Lambda
		//

		public function curlLambda($api) {
			if (strcmp($api, "GetView") == 0) {
				$apiGateway = "https://bljhu5n7wk.execute-api.us-east-1.amazonaws.com/DOTA2_Wiki_GetView";
				$this->query["API"] = "GetView";
				$queryJson = json_encode($this->query);

				$curl = curl_init($apiGateway);
				curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
				curl_setopt($curl, CURLOPT_POSTFIELDS, $queryJson);
				curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
				curl_setopt($curl, CURLOPT_HTTPHEADER, array(
					'Content-Type: application/json',
					'Content-Length: '.strlen($queryJson))
				);
				$recJson = curl_exec($curl);
				$this->rec = json_decode($recJson, true)["response"]["results"];
			}
			if (strcmp($api, "GetRec") == 0) {
				$apiGateway = "https://hk5ae7gzha.execute-api.us-east-1.amazonaws.com/DOTA2_Wiki_GetRec";
				$this->query["API"] = "GetRec";
				$queryJson = json_encode($this->query);

				$curl = curl_init($apiGateway);
				curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
				curl_setopt($curl, CURLOPT_POSTFIELDS, $queryJson);
				curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
				curl_setopt($curl, CURLOPT_HTTPHEADER, array(
					'Content-Type: application/json',
					'Content-Length: '.strlen($queryJson))
				);
				curl_exec($curl);
			}
			if (strcmp($api, "GetCache") == 0) {
				$apiGateway = "https://4ls22l3yvc.execute-api.us-east-1.amazonaws.com/DOTA2_Wiki_GetCache";
				$this->query["API"] = "GetCache";
				$queryJson = json_encode($this->query);

				$curl = curl_init($apiGateway);
				curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
				curl_setopt($curl, CURLOPT_POSTFIELDS, $queryJson);
				curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
				curl_setopt($curl, CURLOPT_HTTPHEADER, array(
					'Content-Type: application/json',
					'Content-Length: '.strlen($queryJson))
				);
				$cacheJson = curl_exec($curl);
				$this->cache = json_decode($cacheJson, true)["response"]["results"];
			}
			if (strcmp($api, "PutCache") == 0) {
				$apiGateway = "https://dj1qnwiknl.execute-api.us-east-1.amazonaws.com/DOTA2_Wiki_PutCache";
				$this->query["API"] = "PutCache";
				$queryJson = json_encode($this->query);

				$curl = curl_init($apiGateway);
				curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
				curl_setopt($curl, CURLOPT_POSTFIELDS, $queryJson);
				curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
				curl_setopt($curl, CURLOPT_HTTPHEADER, array(
					'Content-Type: application/json',
					'Content-Length: '.strlen($queryJson))
				);
				curl_exec($curl);
			}
			if (strcmp($api, "DeleteCache") == 0) {
				$apiGateway = "https://kwnko4wo9i.execute-api.us-east-1.amazonaws.com/DOTA2_Wiki_DeleteCache";
				$this->query["API"] = "DeleteCache";
				$queryJson = json_encode($this->query);

				$curl = curl_init($apiGateway);
				curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
				curl_setopt($curl, CURLOPT_POSTFIELDS, $queryJson);
				curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
				curl_setopt($curl, CURLOPT_HTTPHEADER, array(
					'Content-Type: application/json',
					'Content-Length: '.strlen($queryJson))
				);
				curl_exec($curl);
			}
			if (strcmp($api, "DropCache") == 0) {
				$apiGateway = "https://6vk8v1emya.execute-api.us-east-1.amazonaws.com/DOTA2_Wiki_DropCache";
				$this->query["API"] = "DropCache";
				$queryJson = json_encode($this->query);

				$curl = curl_init($apiGateway);
				curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
				curl_setopt($curl, CURLOPT_POSTFIELDS, $queryJson);
				curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
				curl_setopt($curl, CURLOPT_HTTPHEADER, array(
					'Content-Type: application/json',
					'Content-Length: '.strlen($queryJson))
				);
				curl_exec($curl);
			}
			if (strcmp($api, "GetHero") == 0) {
				$apiGateway = "https://9g432d3dmf.execute-api.us-east-1.amazonaws.com/DOTA2_Wiki_GetHero";
				$this->query["API"] = "GetHero";
				$queryJson = json_encode($this->query);

				$curl = curl_init($apiGateway);
				curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
				curl_setopt($curl, CURLOPT_POSTFIELDS, $queryJson);
				curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
				curl_setopt($curl, CURLOPT_HTTPHEADER, array(
					'Content-Type: application/json',
					'Content-Length: '.strlen($queryJson))
				);
				$desJson = curl_exec($curl);
				$this->des = json_decode($desJson, true)["response"]["results"];
			}
			if (strcmp($api, "GetAbility") == 0) {
				$apiGateway = "https://5vnt1qjyye.execute-api.us-east-1.amazonaws.com/DOTA2_Wiki_GetAbility";
				$this->query["API"] = "GetAbility";
				$queryJson = json_encode($this->query);

				$curl = curl_init($apiGateway);
				curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
				curl_setopt($curl, CURLOPT_POSTFIELDS, $queryJson);
				curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
				curl_setopt($curl, CURLOPT_HTTPHEADER, array(
					'Content-Type: application/json',
					'Content-Length: '.strlen($queryJson))
				);
				$desJson = curl_exec($curl);
				$this->des = json_decode($desJson, true)["response"]["results"];

				for ($i=0; $i<count($this->des); $i++) {
					if (!array_key_exists("CD", $this->des[$i])) {
						$this->des[$i]["CD"] = "Null";
					}
				}
			}
			if (strcmp($api, "GetItem") == 0) {
				$apiGateway = "https://32333vhsfi.execute-api.us-east-1.amazonaws.com/DOTA2_Wiki_GetItem";
				$this->query["API"] = "GetItem";
				$queryJson = json_encode($this->query);

				$curl = curl_init($apiGateway);
				curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
				curl_setopt($curl, CURLOPT_POSTFIELDS, $queryJson);
				curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
				curl_setopt($curl, CURLOPT_HTTPHEADER, array(
					'Content-Type: application/json',
					'Content-Length: '.strlen($queryJson))
				);
				$desJson = curl_exec($curl);
				$this->des = json_decode($desJson, true)["response"]["results"];
				
				for ($i=0; $i<count($this->des); $i++) {
					if (!array_key_exists("Upgrade", $this->des[$i])) {
						$this->des[$i]["Upgrade"] = "Null";
					}
					if (!array_key_exists("Price", $this->des[$i])) {
						$this->des[$i]["Price"] = "Null";
					}
				}
			}
			if (strcmp($api, "GetPlayer") == 0) {
				$apiGateway = "https://epsuuci7ck.execute-api.us-east-1.amazonaws.com/DOTA2_Wiki_GetPlayer";
				$this->query["API"] = "GetPlayer";
				$queryJson = json_encode($this->query);

				$curl = curl_init($apiGateway);
				curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
				curl_setopt($curl, CURLOPT_POSTFIELDS, $queryJson);
				curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
				curl_setopt($curl, CURLOPT_HTTPHEADER, array(
					'Content-Type: application/json',
					'Content-Length: '.strlen($queryJson))
				);
				$desJson = curl_exec($curl);
				$this->des = json_decode($desJson, true)["response"]["results"];
			}
		} 

		//
		// Generate rec table
		//

		public function generateRecTable() {
			$rec = $this->rec;
			if ($rec == null) {
				$rec = array();
			}

			$recTable = <<<EOF
				<div class="recTable">
					<table class="table table-hover">
						<thead class="thead-light">
							<tr>
							<th scope="col">#</th>
							<th scope="col">Hero Name</th>
							<th scope="col">Core Ability</th>
							<th scope="col">Core Item</th>
							<th scope="col">Winning Rate</th>
							</tr>
						</thead>
						<tbody>
			EOF;

			for ($i=count($rec); $i>0; $i--) {
				for ($j=0; $j<$i-1; $j++) {
					if ($rec[$j]["WinningRate"] < $rec[$j+1]["WinningRate"]) {
						$tmp = $rec[$j]; 
						$rec[$j] = $rec[$j+1]; 
						$rec[$j+1] = $tmp; 
					}
				}
			}

			for ($i=0; $i<count($rec); $i++) {
				$row_i = $i+1;
				$heroName = $rec[$i]["HeroName"];
				$ability = $rec[$i]["Ability"];
				$item = $rec[$i]["Item"];
				$winningRate = $rec[$i]["WinningRate"];
				
				$row = <<<EOF
					<tr>
						<th scope="row">$row_i</th>
						<td>
							<a href="description.php?HeroName=$heroName">$heroName</a>
						</td>
						<td>
							<a href="description.php?CoreAbility=$ability">$ability</a>
						</td>
						<td>
							<a href="description.php?CoreItem=$item">$item</a>
						</td>
						<td>
							$winningRate
						</td>
					</tr>
				EOF;

				$recTable = $recTable.$row;
			} 

			$recTable = $recTable.<<<EOF
						</tbody>
					</table>
					<div class="btn-group">
						<button type="submit" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							Add
						</button>
						<div class="dropdown-menu">
			EOF;

			for ($i=0; $i<count($rec); $i++) {
				$heroName = $rec[$i]["HeroName"];
				
				$row = <<<EOF
						<a class="dropdown-item" href="search.php?AddHero=$heroName">$heroName</a>
				EOF;
				$recTable = $recTable.$row;
			} 
							
			$recTable = $recTable.<<<EOF
							<div class="dropdown-divider"></div>
							<a class="dropdown-item" href="search.php?AddHero=All">All</a>
						</div>
					</div>
				</div>
			EOF;
			
			return $recTable;
		}

		//
		// Generate cache table
		//

		public function generateCacheTable() {
			$cache = $this->cache;
			if ($cache == null) {
				$cache = array();
			}

			$cacheTable = <<<EOF
				<div class="cacheTable">
					<table class="table table-hover">
						<thead class="thead-light">
							<tr>
							<th scope="col">#</th>
							<th scope="col">Hero Name</th>
							<th scope="col">Core Ability</th>
							<th scope="col">Core Item</th>
							<th scope="col">Winning Rate</th>
							</tr>
						</thead>
						<tbody>
			EOF;

			for ($i=count($cache); $i>0; $i--) {
				for ($j=0; $j<$i-1; $j++) {
					if ($cache[$j]["WinningRate"] < $cache[$j+1]["WinningRate"]) {
						$tmp = $cache[$j]; 
						$cache[$j] = $cache[$j+1]; 
						$cache[$j+1] = $tmp; 
					}
				}
			}

			for ($i=0; $i<count($cache); $i++) {
				$row_i = $i + 1;
				$heroName = $cache[$i]["HeroName"];
				$ability = $cache[$i]["Ability"];
				$item = $cache[$i]["Item"];
				$winningRate = $cache[$i]["WinningRate"];
				
				$row = <<<EOF
					<tr>
					<th scope="row">$row_i</th>
						<td>
							<a href="description.php?HeroName=$heroName">$heroName</a>
						</td>
						<td>
							<a href="description.php?CoreAbility=$ability">$ability</a>
						</td>
						<td>
							<a href="description.php?CoreItem=$item">$item</a>
						</td>
						<td>
							$winningRate
						</td>
					</tr>
				EOF;
				$cacheTable = $cacheTable.$row;
			} 

			$cacheTable = $cacheTable.<<<EOF
						</tbody>
					</table>
					<div class="btn-group">
						<button type="submit" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							Delete
						</button>
						<div class="dropdown-menu">
			EOF;

			for ($i=0; $i<count($cache); $i++) {
				$heroName = $cache[$i]["HeroName"];
				
				$row = <<<EOF
					<a class="dropdown-item" href="search.php?DeleteHero=$heroName">$heroName</a>
				EOF;
				$cacheTable = $cacheTable.$row;
			} 
							
			$cacheTable = $cacheTable.<<<EOF
							<div class="dropdown-divider"></div>
							<a class="dropdown-item" href="search.php?DeleteHero=All">All</a>
						</div>
					</div>
				</div>
			EOF;

			return $cacheTable;
		}

		// 
		// Generate description table
		//

		function generateDesTable() {
			$query = $this->query;
			$desTable = <<<EOF
				<div class="descriptionTable">
						<table class="table table-hover">
							<thead class="thead-light">
			EOF;

			if (array_key_exists("HeroName", $query)) {
				$this->curlLambda("GetHero");
				$desTable = $desTable.<<<EOF
								<tr>
								<th scope="col">Hero Name</th>
								<th scope="col">Primary Attribute</th>
								<th scope="col">Fraction</th>
								<th scope="col">Ability</th>
								<th scope="col">Item</th>
								<th scope="col">Hero Type</th>
								<th scope="col">Complexity</th>
								<th scope="col">Winning Rate</th>
								<th scope="col">Player Name</th>
								</tr>
							</thead>
							<tbody>
				EOF;
			}
			if (array_key_exists("AbilityName", $query)) {
				$this->curlLambda("GetAbility");
				$desTable = $desTable.<<<EOF
								<tr>
								<th scope="col">Ability Name</th>
								<th scope="col">Hero Name</th>
								<th scope="col">Ability Type</th>
								<th scope="col">CD</th>
								</tr>
							</thead>
							<tbody>
				EOF;
			}
			if (array_key_exists("ItemName", $query)) {
				$this->curlLambda("GetItem");
				$desTable = $desTable.<<<EOF
								<tr>
								<th scope="col">Item Name</th>
								<th scope="col">Price</th>
								<th scope="col">Upgrade</th>
								<th scope="col">Item Type</th>
								</tr>
							</thead>
							<tbody>
				EOF;
			}
			if (array_key_exists("PlayerName", $query)) {
				$this->curlLambda("GetPlayer");
				$desTable = $desTable.<<<EOF
								<tr>
								<th scope="col">Player Name</th>
								<th scope="col">Representative</th>
								<th scope="col">Team</th>
								<th scope="col">Famous Scene</th>
								</tr>
							</thead>
							<tbody>
				EOF;
			}

			$des = $this->des;

			if (array_key_exists("HeroName", $query)) {
				for ($i=0; $i<count($des); $i++) {
					$row_i = $i+1;
					$heroName = $des[$i]["HeroName"];
					$primaryAttribute = $des[$i]["PrimaryAttribute"];
					$fraction = $des[$i]["Fraction"];
					$ability = $des[$i]["Ability"];
					$item = $des[$i]["Item"];
					$heroType = $des[$i]["Hero Type"];
					$complexity = $des[$i]["Complexity"];
					$winningRate = $des[$i]["WinningRate"];
					$playerName = $des[$i]["PlayerName"];
					
					$row = <<<EOF
						<tr>
							<td>$heroName</td>
							<td>$primaryAttribute</td>
							<td>$fraction</td>
							<td>$ability</td>
							<td>$item</td>
							<td>$heroType</td>
							<td>$complexity</td>
							<td>$winningRate</td>
							<td>
								<a href="description.php?PlayerName=$playerName">$playerName</a>
							</td>
						</tr>
					EOF;
					$desTable = $desTable.$row;
				} 
			}
			if (array_key_exists("AbilityName", $query)) {
				for ($i=0; $i<count($des); $i++) {
					$row_i = $i+1;
					$abilityName = $des[$i]["AbilityName"];
					$heroName = $des[$i]["HeroName"];
					$abilityType = $des[$i]["AbilityType"];
					$cd = $des[$i]["CD"];
					
					$row = <<<EOF
						<tr>
							<td>$abilityName</td>
							<td>$heroName</td>
							<td>$abilityType</td>
							<td>$cd</td>
						</tr>
					EOF;
					$desTable = $desTable.$row;
				} 
			}
			if (array_key_exists("ItemName", $query)) {
				for ($i=0; $i<count($des); $i++) {
					$row_i = $i+1;
					$itemName = $des[$i]["ItemName"];
					$price = $des[$i]["Price"];
					$upgrade = $des[$i]["Upgrade"];
					$itemType = $des[$i]["ItemType"];
					
					$row = <<<EOF
						<tr>
							<td>$itemName</td>
							<td>$price</td>
							<td>$upgrade</td>
							<td>$itemType</td>
						</tr>
					EOF;
					$desTable = $desTable.$row;
				} 
			}
			if (array_key_exists("PlayerName", $query)) {
				for ($i=0; $i<count($des); $i++) {
					$row_i = $i+1;
					$playerName = $des[$i]["PlayerName"];
					$representative = $des[$i]["Representative"];
					$team = $des[$i]["Team"];
					$famousScene = $des[$i]["FamousScene"];
					$webLink = $des[$i]["WebLink"];
					if ($webLink == null) {
						$webLink = "https://www.youtube.com/watch?v=lL-YDyq2RUw";
					}

					$row = <<<EOF
						<tr>
							<td>$playerName</td>
							<td>$representative</td>
							<td>$team</td>
							<td>
								<a href="$webLink" target="view_window">$famousScene</a>
							</td>
						</tr>
					EOF;
					$desTable = $desTable.$row;
				} 
			}
							
			$desTable = $desTable.<<<EOF
						</div>
					</div>
				</div>
			EOF;
			
			return $desTable;
		}

		// 
		// Add designated elements to cache
		//

		public function addCache($heroName) {
			$query = array();
			$query["HeroName"] = $heroName;
			$this->query = $query;

			$this->curlLambda("PutCache");
		}

		// 
		// Delete designated elements from cache
		//

		public function deleteCache($heroName) {
			$query = array();
			$query["HeroName"] = $heroName;
			$this->query = $query;

			if ($heroName != "All") {
				$this->curlLambda("DeleteCache");
			} else {
				$this->curlLambda("DropCache");
			}
		}
	}	

?>
