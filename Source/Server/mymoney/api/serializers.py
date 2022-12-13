from rest_framework import serializers
from core.models import *

class wallets_serializer(serializers.ModelSerializer):
    class Meta:
        model = wallets
        fields = '__all__'

class wallets_types_serializer(serializers.ModelSerializer):
    class Meta:
        model = wallets_types
        fields = '__all__'