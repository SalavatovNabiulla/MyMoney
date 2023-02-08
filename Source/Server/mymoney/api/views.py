from rest_framework.response import Response
from rest_framework.decorators import api_view
from core.models import *
from .serializers import *

#++wallets

@api_view(['POST'])
def update_wallets_type(request):
    current_wallets_type = wallets_types.objects.get(pk=request.data['id'])
    current_wallets_type.title = request.data['title']
    current_wallets_type.save()
    serializer = wallets_types_serializer(current_wallets_type)
    return Response(serializer.data)

@api_view(['POST'])
def create_wallets_type(request):
    serializer = wallets_types_serializer(data=request.data)
    if serializer.is_valid():
        serializer.save()
    return Response(serializer.data)


@api_view(['GET'])
def get_wallets_types(request):
    wallets_types_list = wallets_types.objects.all()
    serializer = wallets_types_serializer(wallets_types_list,many=True)
    return Response(serializer.data)

@api_view(["POST"])
def get_wallets_type(request):
    wallets_type = wallets_types.objects.get(pk=request.data["id"])
    serializer = wallets_types_serializer(wallets_type)
    return Response(serializer.data)

@api_view(['POST'])
def delete_wallets_type(request):
    wallets_type = wallets_types.objects.get(pk=request.data['id'])
    wallets_type.delete()
    response = {"result":True}
    return Response(response)

@api_view(['POST'])
def update_wallet(request):
    current_wallet = wallets.objects.get(pk=request.data['id'])
    current_wallet.title = request.data['title']
    current_wallet.type_id = wallets_types.objects.get(pk=request.data['type_id'])
    current_wallet.save()
    serializer = wallets_serializer(current_wallet)
    return Response(serializer.data)

@api_view(['POST'])
def delete_wallet(request):
    wallet = wallets.objects.get(pk=request.data['id'])
    wallet.delete()
    response = {"result":True}
    return Response(response)

@api_view(['POST'])
def create_wallet(request):
    serializer = wallets_serializer(data = request.data)
    if serializer.is_valid():
        serializer.save()
        wallet = wallets.objects.get(pk=int(serializer.data['id']))
        balance = wallets_balances.objects.create(wallet_id=wallet,balance=0)
    return Response(serializer.data)

@api_view(['GET'])
def get_wallets(request):
    wallets_list = wallets.objects.all()
    serializer = wallets_serializer(wallets_list,many=True)
    return Response(serializer.data)

@api_view(["POST"])
def get_wallet(request):
    wallet = wallets.objects.get(pk=request.data["id"])
    serializer = wallets_serializer(wallet)
    return Response(serializer.data)

@api_view(['GET'])
def get_wallets_balances(request):
    wallets_balances_list = wallets_balances.objects.all()
    serializer = wallets_balances_serializer(wallets_balances_list,many=True)
    return  Response(serializer.data)

@api_view(["POST"])
def get_wallets_balance(request):
    wallets_balance = wallets_balances.objects.get(wallet_id=request.data["wallet_id"])
    serializer = wallets_balances_serializer(wallets_balance)
    return Response(serializer.data)

##--wallets

##++transactions

@api_view(['POST'])
def delete_transactions_type(request):
    transactions_type = transactions_types.objects.get(pk=request.data["id"])
    transactions_type.delete()
    response = {"result":True}
    return Response(response)

@api_view(['POST'])
def create_transactions_type(request):
    serializer = transactions_types_serializer(data=request.data)
    if serializer.is_valid():
        serializer.save()
    return Response(serializer.data)

@api_view(['GET'])
def get_transactions_types(request):
    transactions_types_list = transactions_types.objects.all()
    serializer = transactions_types_serializer(transactions_types_list,many=True)
    return Response(serializer.data)

@api_view(["POST"])
def get_transactions_type(request):
    transactions_type = transactions_types.objects.get(pk=request.data["id"])
    serializer = transactions_types_serializer(transactions_type)
    return Response(serializer.data)

@api_view(['POST'])
def delete_transaction(request):
    transaction = transactions.objects.get(pk=request.data['id'])
    wallets_balance = wallets_balances.objects.get(wallet_id=transaction.wallet_id)
    transaction_type = transactions_types.objects.get(pk=transaction.type_id.id)
    if transaction_type.title == "income":
        wallets_balance.balance = wallets_balance.balance - transaction.sum
    else:
        wallets_balance.balance = wallets_balance.balance + transaction.sum
    wallets_balance.save(update_fields=['balance'])
    transaction.delete()
    response = {"result":True}
    return Response(response)

@api_view(['POST'])
def create_transaction(request):
    serializer = transactions_serializer(data=request.data)
    if serializer.is_valid():
        serializer.save()
        wallets_balance = wallets_balances.objects.get(wallet_id=serializer.data['wallet_id'])
        transaction_type = transactions_types.objects.get(pk=serializer.data['type_id'])
        if transaction_type.title == "income":
            wallets_balance.balance = wallets_balance.balance + serializer.data['sum']
        else:
            wallets_balance.balance = wallets_balance.balance - serializer.data['sum']
        wallets_balance.save(update_fields=['balance'])
    return Response(serializer.data)

@api_view(['POST'])
def update_transaction(request):
    current_transaction = transactions.objects.get(pk=request.data['id'])
    wallets_balance = wallets_balances.objects.get(wallet_id=current_transaction.wallet_id)
    #
    old_sum = current_transaction.sum
    old_type = current_transaction.type_id
    #
    current_transaction.type_id = transactions_types.objects.get(pk=int(request.data['type_id']))
    current_transaction.sum = int(request.data['sum'])
    current_transaction.wallet_id = wallets.objects.get(pk=int(request.data['wallet_id']))
    try:
        current_transaction.revenue_item_id = revenue_items.objects.get(pk=int(request.data['revenue_item_id']))
    except:
        current_transaction.cost_item_id = cost_items.objects.get(pk=int(request.data['cost_item_id']))
    current_transaction.save()
    #
    new_sum = current_transaction.sum
    new_type = current_transaction.type_id
    #
    if old_type.id == new_type.id:
        if new_type.title == "income":
            wallets_balance.balance = wallets_balance.balance - old_sum
            wallets_balance.balance = wallets_balance.balance + new_sum
        else:
            wallets_balance.balance = wallets_balance.balance + old_sum
            wallets_balance.balance = wallets_balance.balance - new_sum
    else:
        if new_type.title == "income":
            wallets_balance.balance = wallets_balance.balance + old_sum
            wallets_balance.balance = wallets_balance.balance + new_sum
        else:
            wallets_balance.balance = wallets_balance.balance - old_sum
            wallets_balance.balance = wallets_balance.balance - new_sum

    wallets_balance.save(update_fields=['balance'])
    #
    serializer = transactions_serializer(current_transaction)
    return Response(serializer.data)

@api_view(['GET'])
def get_transactions(request):
    transactions_list = transactions.objects.all()
    json_array = []
    for i in transactions_list:
        json_transaction = {}
        json_transaction["id"] = i.id
        json_transaction["created_time"] = i.created_time
        json_transaction["sum"] = i.sum
        #+
        json_wallet = {}
        json_wallet["id"] = i.wallet_id.id
        json_wallet["title"] = i.wallet_id.title
        ##+
        json_wallet_type = {}
        json_wallet_type["id"] = i.wallet_id.type_id.id
        json_wallet_type["title"] = i.wallet_id.type_id.title
        ##-
        json_wallet["type"] = json_wallet_type
        #-
        json_transaction["wallet"] = json_wallet
        json_transaction_type = {}
        json_transaction_type["id"] = i.type_id.id
        json_transaction_type["title"] = i.type_id.title
        json_transaction["type"] = json_transaction_type
        #
        json_revenue_item = {}
        json_cost_item = {}
        if i.revenue_item_id != None:
            json_revenue_item["id"] = i.revenue_item_id.id
            json_revenue_item["title"] = i.revenue_item_id.title
        if i.cost_item_id != None:
            json_cost_item["id"] = i.cost_item_id.id
            json_cost_item["title"] = i.cost_item_id.title
        json_transaction["revenue_item"] = json_revenue_item
        json_transaction["cost_item"] = json_cost_item
        #
        json_array.append(json_transaction)
    return Response(json_array)

@api_view(["POST"])
def get_transaction(request):
    transaction = transactions.objects.get(pk=request.data["id"])
    json_transaction = {}
    json_transaction["id"] = transaction.id
    json_transaction["created_time"] = transaction.created_time
    json_transaction["sum"] = transaction.sum
    # +
    json_wallet = {}
    json_wallet["id"] = transaction.wallet_id.id
    json_wallet["title"] = transaction.wallet_id.title
    ##+
    json_wallet_type = {}
    json_wallet_type["id"] = transaction.wallet_id.type_id.id
    json_wallet_type["title"] = transaction.wallet_id.type_id.title
    ##-
    json_wallet["type"] = json_wallet_type
    # -
    json_transaction["wallet"] = json_wallet
    json_transaction_type = {}
    json_transaction_type["id"] = transaction.type_id.id
    json_transaction_type["title"] = transaction.type_id.title
    json_transaction["type"] = json_transaction_type
    #
    json_revenue_item = {}
    json_cost_item = {}
    if transaction.revenue_item_id != None:
        json_revenue_item["id"] = transaction.revenue_item_id.id
        json_revenue_item["title"] = transaction.revenue_item_id.title
    if transaction.cost_item_id != None:
        json_cost_item["id"] = transaction.cost_item_id.id
        json_cost_item["title"] = transaction.cost_item_id.title
    json_transaction["revenue_item"] = json_revenue_item
    json_transaction["cost_item"] = json_cost_item
    #
    return Response(json_transaction)


##--transactions

##++revenue items

@api_view(['GET'])
def get_revenue_items(request):
    revenue_items_list = revenue_items.objects.all()
    serializer = revenue_items_serializer(revenue_items_list,many=True)
    return Response(serializer.data)

@api_view(["POST"])
def get_revenue_item(request):
    revenue_item = revenue_items.objects.get(pk=request.data["id"])
    serializer = revenue_items_serializer(revenue_item)
    return Response(serializer.data)

@api_view(['POST'])
def create_revenue_item(request):
    serializer = revenue_items_serializer(data=request.data)
    if serializer.is_valid():
        serializer.save()
    return Response(serializer.data)

@api_view(['POST'])
def delete_revenue_item(request):
    revenue_item = revenue_items.objects.get(pk=request.data['id'])
    revenue_item.delete()
    response = {"result":True}
    return Response(response)

@api_view(['POST'])
def update_revenue_item(request):
    current_revenue_item = revenue_items.objects.get(pk=request.data['id'])
    current_revenue_item.title = request.data['title']
    current_revenue_item.save()
    serializer = revenue_items_serializer(current_revenue_item)
    return Response(serializer.data)

##--revenue items

##++cost items

@api_view(['GET'])
def get_cost_items(request):
    cost_items_list = cost_items.objects.all()
    serializer = cost_items_serializer(cost_items_list,many=True)
    return Response(serializer.data)

@api_view(["POST"])
def get_cost_item(request):
    cost_item = cost_items.objects.get(pk=request.data["id"])
    serializer = cost_items_serializer(cost_item)
    return Response(serializer.data)


@api_view(['POST'])
def create_cost_item(request):
    serializer = cost_items_serializer(data=request.data)
    if serializer.is_valid():
        serializer.save()
    return Response(serializer.data)

@api_view(['POST'])
def delete_cost_item(request):
    revenue_item = cost_items.objects.get(pk=request.data['id'])
    revenue_item.delete()
    response = {"result":True}
    return Response(response)

@api_view(['POST'])
def update_cost_item(request):
    current_cost_item = cost_items.objects.get(pk=request.data['id'])
    current_cost_item.title = request.data['title']
    current_cost_item.save()
    serializer = cost_items_serializer(current_cost_item)
    return Response(serializer.data)

##--cost items