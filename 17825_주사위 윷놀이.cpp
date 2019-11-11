#include <cstdio>
#include <cstring>
#include <vector>
#include <algorithm>
#define START 0
#define END 32
#pragma warning(disable:4996)
using namespace std;

int ans = -1, dice[10], horse[4];
int score[33] = {
	0, 2, 4, 6, 8, 10, 12, 14, 16, 18,
	20, 22, 24, 26, 28, 30, 32, 34, 36, 38,
	40, 22, 24, 25, 30, 35, 13, 16, 19, 28, 
	27, 26, 0
};
int index(int idx, int cur) {
	if (cur == 1) {
		if (idx == 5) return 25;
		if (idx == 10) return 20;
		if (idx == 15) return 28;
	}
	if (idx == 20) return 31;
	if (idx == 25) return 19;
	if (idx == 28) return 22;
	if (idx == 31) return 22;
	return idx;
}
bool is_exist(int idx)
{
	for (int i = 0; i < 4; i++) {
		if (idx != i && horse[idx] == horse[i]) return false;
	}
	return true;
}
void solve(int cnt, int sum)
{
	int prev;
	if (cnt == 10) {
		ans = max(ans, sum);
		return;
	}
	for (int i = 0; i < 4; i++) {
		if (horse[i] < END) {
			prev = horse[i];
			for (int j = 1; j <= dice[cnt]; j++) {
				horse[i] = index(horse[i], j);
				horse[i]++;
			}
			if (!is_exist(i)) {
				horse[i] = prev;
				continue;
			}
			if (horse[i] > END) horse[i] = END;
			solve(cnt + 1, sum + score[horse[i]]);
			horse[i] = prev;
		}
	}
}
int main()
{
	for (int i = 0; i < 10; i++) scanf("%d", &dice[i]);
	solve(0, 0);
	printf("%d\n", ans);
	return 0;
}