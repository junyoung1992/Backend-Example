from django import template
from django.db.models import F
from django.http import HttpResponse
from django.http.response import Http404, HttpResponseRedirect
from django.shortcuts import get_object_or_404, render
from django.template import loader
from django.urls import reverse
from django.views import generic

from .models import Choice, Question


"""
# 가장 간단한 형태의 뷰
# 뷰를 호출하려면 이와 연결된 URL이 있어야 하는데, 이를 위해 URLconf가 사용
# -> urls.py
def index(request):
    latest_question_list = Question.objects.order_by('-pub_date')[:5]
    # template = loader.get_template('polls/index.html')
    context = {
        'latest_question_list': latest_question_list,
    }
    # return HttpResponse(template.render(context, request))
    
    # render(request,
    #        template_name,
    #        context (optional)
    # )
    # context로 표현된 템플릿의 HttpResponse 객체가 반환
    return render(request, 'polls/index.html', context)
"""
# ListView는 객체 목록 표시 개념을 추상화
class IndexView(generic.ListView):
    template_name = 'polls/index.html'
    context_object_name = 'latest_question_list'
    
    def get_queryset(self):
        """Return the last five published questions."""
        return Question.objects.order_by('-pub_date')[:5]

"""
def detail(request, question_id):
    # try:
    #     question = Question.objects.get(pk=question_id)
    # except Question.DoesNotExist:
    #     raise Http404("Question does not exist.")
    
    # get_object_or_404() 함수는 Django 모델을 첫번재 인자로 받고,
    # 몇개의 키워드 인수를 관리자의 get() 함수에 넘긴다.
    # 만약 객체가 존재하지 않을 경우, Http404 예외가 발생한다.
    
    # django의 주요한 설계 목표가 모델 계층과 뷰 계층의 약결합이기 때문에
    # 상위 계층에서 ObjectDoesNotExist 예외를 자동으로 잡아내지 않고
    # view 계층에서 예외를 잡아낸다.
    question = get_object_or_404(Question, pk=question_id)
    return render(request, 'polls/detail.html', {'question': question})
"""
# DetailView는 특정 객체 유형에 대한 세부 정보 페이지 표시 개념을 추상화
# URL에 캡쳐된 기본 키 값이 "pk"라고 기대하기 때문에 urls의 question_id를
# pk로 변경
class DetailView(generic.DetailView):
    model = Question
    template_name = 'polls/detail.html'

"""
def results(request, question_id):
    question = get_object_or_404(Question, pk=question_id)
    return render(request, 'polls/results.html', {'question': question})
"""
class ResultsView(generic.DetailView):
    model = Question
    template_name = 'polls/results.html'

def vote(request, question_id):
    question = get_object_or_404(Question, pk=question_id)
    
    try:
        # request.POST는 키로 전송된 자료에 접근할 수 있도록 해주는 객체
        selected_choice = question.choice_set.get(pk=request.POST['choice'])
    except (KeyError, Choice.DoesNotExist):
        # Redisplay the question voting form.
        return render(
            request,
            'polls/detail.html',
            {
                'question' : question,
                'error_message': "You didn't select a choice."
            }
        )
    else:
        # selected_choice.votes += 1
        # Avoid race condition
        selected_choice.votes = F('votes') + 1
        selected_choice.save()
        
        # Always return an HttpResponseRedirect after successfully dealing
        # with POST data. This prevents data from being posted twice if a
        # user hits the Back button.
        
        # reverse 함수는 url을 하드코딩하지 않도록 도와줌
        return HttpResponseRedirect(
            # -> /polls/3/results
            reverse('polls:results', args=(question.id,)) 
        )

