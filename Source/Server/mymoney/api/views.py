from rest_framework.response import Response
from rest_framework.decorators import api_view
from core.models import *
from .serializers import *

#++wallets

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

@api_view(['GET'])
def get_wallets_balances(request):
    wallets_balances_list = wallets_balances.objects.all()
    serializer = wallets_balances_serializer(wallets_balances_list,many=True)
    return  Response(serializer.data)
##--wallets

##++transactions

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

@api_view(['POST'])
def create_transaction(request):
    serializer = transactions_serializer(data=request.data)
    if serializer.is_valid():
        serializer.save()
    return Response(serializer.data)

@api_view(['GET'])
def get_transactions(request):
    transactions_list = transactions.objects.all()
    serializer = transactions_serializer(transactions_list,many=True)
    return Response(serializer.data)
##--transactions