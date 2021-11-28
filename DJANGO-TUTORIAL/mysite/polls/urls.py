from django.urls import path

from . import views

app_name = 'polls'
urlpatterns = [
    # path(route, view, kwargs, name)
    # route:
    #   url 패턴을 가진 문자열.
    #   urlpatterns 의 첫 번째 패턴부터 시작하여, 일치하는 패턴을 찾을 때 까지 요청된 URL을 각 패턴과 리스트의 순서대로 비교한다.
    # view:
    #   일치하는 패턴을 찾으면, HttpRequest 객체를 첫번째 인수로 하고,
    #   경로로 부터 캡쳐된 값을 키워드 인수로하여 특정한 view 함수를 호출한다.
    # kwargs:
    #   임의의 키워드 인수들은 목표한 view 에 사전형으로 전달된다.
    # name:
    #   URL 에 이름을 지으면, 템플릿을 포함한 Django 어디에서나 명확하게 참조할 수 있다.
    #   이를 통해, 단 하나의 파일만 수정해도 project 내의 모든 URL 패턴을 바꿀 수 있다.
    path('', views.IndexView.as_view(), name='index'),
    path('<int:pk>/', views.DetailView.as_view(), name='detail'),    # detail(request = <HttpRequest object>, question_id = 34)
    path('<int:pk>/results/', views.ResultsView.as_view(), name='results'),
    path('<int:question_id>/vote/', views.vote, name='vote'),
]