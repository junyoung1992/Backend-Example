"""mysite URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/3.2/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from django.urls.conf import include

urlpatterns = [
    # include() 함수는 다른 URLconf들을 참조할 수 있도록 도와줌
    # Djaongo가 함수 include()를 만나게 되면, URL의 그 시점까지 일치하는 부분을 잘라내고,
    # 남은 문자열 부분을 후속 처리를 위해 include 된 URLconf로 전달한다.
    path('polls/', include('polls.urls')),
    path('admin/', admin.site.urls),
]
