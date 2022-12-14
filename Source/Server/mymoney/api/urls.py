from django.urls import path
from . import views

urlpatterns = [
    path("create_wallets_type/",views.create_wallets_type),
    path("delete_wallet/",views.delete_wallet),
    path("get_wallets_types/", views.get_wallets_types),
    path("create_wallet/", views.create_wallet),
    path("get_wallets/", views.get_wallets),
    path("get_wallets_balances/", views.get_wallets_balances),
    path("create_transactions_type/", views.create_transactions_type),
    path("get_transactions_types/", views.get_transactions_types),
    path("create_transaction/", views.create_transaction),
    path("delete_transaction/", views.delete_transaction),
    path("get_transactions/", views.get_transactions),
]