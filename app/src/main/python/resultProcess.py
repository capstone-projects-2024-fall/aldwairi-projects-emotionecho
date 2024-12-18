import json

'''
#input: list of file name following the naming convention: the 8th char is the digit of emotion
#output: list of the emotion percentage in order of (1~7)/(Neutral~Disgust)
def get_emotions(list_name):
    list_name = json.loads(list_name)
    list_emotion = list([])
    for name in list_name:
        list_emotion.append(name[7])
    return get_emotions_percentage(list_emotion)
'''
# input: list of digits indicating the emotion, E.x. [1,2,3,4,5,6,6,7,8]
# return: list of emotion percentages in order, E.x. [12.5, 12.5, 12.5, 12.5, 12.5, 25.0, 12.5]
def get_emotions_percentage(list_emotion):

    emotion_mapping = {
        "neutral": 0,
        "calm": 1,
        "happy": 2,
        "sad": 3,
        "angry": 4,
        "fearful": 5,
        "disgust": 6,
        "surprised": 7
    }

    list_emotion = json.loads(list_emotion)
    list_emotion = [emotion_mapping[emotion] for emotion in list_emotion]

    emotions_percentage = [0.0] * 8

    for num in list_emotion:
        emotions_percentage[num] += 1

    for index, count in enumerate(emotions_percentage):
        emotions_percentage[index] = (count / len(list_emotion)) * 100 if list_emotion else 0

    return emotions_percentage


