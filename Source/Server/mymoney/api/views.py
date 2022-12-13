from rest_framework.response import Response
from rest_framework.decorators import api_view
from core.models import wallets
from .serializers import *

#++wallets_types
@api_view(['GET'])
def get_wallets_types(request):
    wallets_types_list = wallets_types.objects.all()
    serializer = wallets_types_serializer(wallets_types_list,many=True)
    return Response(serializer.data)

@api_view(['POST'])
def create_wallets_type(request):
    serializer = wallets_types_serializer(data=request.data)
    if serializer.is_valid():
        serializer.save()
    return Response(serializer.data)
#--wallets_types

#++wallets
@api_view(['GET'])
def get_wallets(request):
    wallets_list = wallets.objects.all()
    serializer = wallets_serializer(wallets_list,many=True)
    return Response(serializer.data)
#--wallets