<html>
<head>
<!--
Project Name : CRUD with PHP, MySQL and Bootstrap
Author		 : Hendra Setiawan
Website		 : http://www.hendrasetiawan.net
Email	 	 : hendrabpp[at]gmail.com
-->
<?php 
include "module/header.php";
include "module/alerts.php";
include "config/connect.php"; 

$sql = mysql_query("SELECT pid, type, detail, street, imei, status, created_at FROM products WHERE pid = '".$_GET['id']."'");
$data = mysql_fetch_array($sql);
?>
</head>
<body>

<div class="container">
<?php include "module/nav.php"; ?>
<div class="row">
    <div class="col-lg-12">
        <div class="page-header">
            <h1>Form Edit (Update)</h1>
        </div>
    </div>
</div>

<div class="row">
	<div class="col-md-6">
	<form id="form_input" method="POST">	

<?php  
if(isset($_POST['update']))
{	
	
	$date = new DateTime();
	$a = $date->format('Y-m-d H:i:s');
	
	
	mysql_query("UPDATE products SET status = '".$_POST['hp']."', updated_at = '".$a."' WHERE pid = '".$_GET['id']."'");
	writeMsg('update.sukses');

	//Re-Load Data from DB
	$sql = mysql_query("SELECT pid, type, detail, street, imei, status, created_at FROM products WHERE pid = '".$_GET['id']."'");
	$data = mysql_fetch_array($sql);
}
?>

	<div class="form-group">
  		<label class="control-label" for="nama">Complain Number : </label>
  		<?php echo $data['pid']; ?>
	</div>

	<div class="form-group">
  		<label class="control-label" for="email">Type : </label>
  		<?php echo $data['type']; ?>
	</div>
	
	<div class="form-group">
  		<label class="control-label" for="email">Street : </label>
  		<?php echo $data['street']; ?>
	</div>
	
	<div class="form-group">
  		<label class="control-label" for="email">IMEI : </label>
  		<?php echo $data['imei']; ?>
	</div>
	
	<div class="form-group">
  		<label class="control-label" for="email">Created at : </label>
  		<?php echo $data['created_at']; ?>
	</div>

	<p> 0 : Complain made<br>1 : Complain under process 
	<div class="form-group">
  		<label class="control-label" for="hp">Complain Status</label>
  		<input type="text" class="form-control" name="hp" id="hp" value="<?php echo $data['status']; ?>">
	</div>

	<div class="form-group">
	<input type="submit" value="Update" name="update" class="btn btn-primary">
	<a href="rekap.php" class="btn btn-danger">Back</a>
	</div>

	</form>
	</div>
</div>

</div>
<?php include "module/footer.php"; ?>
</body>
</html>