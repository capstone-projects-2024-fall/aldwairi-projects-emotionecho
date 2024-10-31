import json
# input: list of name indicating the emotion, E.x. [1,2,3,4,5,6,6,7]
# return: list of emotion percentage in order, E.x. [12.5, 12.5, 12.5, 12.5, 12.5, 25.0, 12.5]
def get_emotions_percentage(list_emotion):
    #need only for testing
    list_emotion = json.loads(list_emotion)
    emotions_percentage = [0.0,0.0,0.0,0.0,0.0,0.0,0.0]
    for num in list_emotion:
        emotions_percentage[num-1] += 1
    for index,count in enumerate(emotions_percentage):
        emotions_percentage[index] = count/len(list_emotion)*100
    return emotions_percentage


def get_emotions(list_name):
    list_name = json.loads(list_name)
    list_emotion = list([])
    for name in list_name:
        list_emotion.append(name[7])
    return get_emotions_percentage(list_emotion)
