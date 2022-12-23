from django.db import models

class wallets_types(models.Model):
    title = models.CharField(max_length=100)

class wallets(models.Model):
    title = models.CharField(max_length=200)
    type_id = models.ForeignKey(wallets_types,on_delete=models.RESTRICT)

class wallets_balances(models.Model):
    wallet_id = models.ForeignKey(wallets,on_delete=models.CASCADE)
    balance = models.IntegerField()

class transactions_types(models.Model):
    title = models.CharField(max_length=200)

class revenue_items(models.Model):
    title = models.CharField(max_length=200)

class cost_items(models.Model):
    title = models.CharField(max_length=200)

class transactions(models.Model):
    wallet_id = models.ForeignKey(wallets,on_delete=models.RESTRICT)
    type_id = models.ForeignKey(transactions_types,on_delete=models.RESTRICT)
    created_time = models.DateTimeField(auto_now_add=True)
    sum = models.IntegerField()
    revenue_item_id = models.ForeignKey(revenue_items,on_delete=models.RESTRICT,null=True,blank=True)
    cost_item_id = models.ForeignKey(cost_items,on_delete=models.RESTRICT,null=True,blank=True)


