from django.urls import path
from . import views

urlpatterns = [
    path("get_wallets/",views.get_wallets),
]