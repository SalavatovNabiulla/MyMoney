from django.urls import path
from . import views

urlpatterns = [
    path("get_wallets/",views.get_wallets),
    path("get_wallets_types/",views.get_wallets_types),
    path("create_wallets_type",views.create_wallets_type),
]