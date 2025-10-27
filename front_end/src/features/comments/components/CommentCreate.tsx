import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import * as z from "zod"
import { Button } from "@/shared/ui/button"
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormMessage,
} from "@/shared/ui/form"
import { Textarea } from "@/shared/ui/textarea"
import { useUser } from "@/features/user/useUserContext"
import { useState } from "react"
interface CommentCreateProps {
    onSubmit: (content: string) => Promise<void>;
}
const formSchema = z.object({
    content: z.string().min(1, "Comment cannot be empty").max(280, "Comment must be at most 280 characters"),
});

export default function CommentCreate({ onSubmit }: CommentCreateProps) {
    const { user } = useUser();
    const [loading, setLoading] = useState(false); 
    const form = useForm<z.infer<typeof formSchema>>({
      resolver: zodResolver(formSchema),
      defaultValues: { content: "" },
    });
  
    if (user.isGuest) {
      return (
        <div className="w-5/6 mx-auto p-4 border">
          <p className="text-red-500">
            Only users can comment. Please register as a user to comment.
          </p>
        </div>
      );
    }
  
    async function handleSubmit(values: z.infer<typeof formSchema>) {
      setLoading(true);
      await onSubmit(values.content);
      form.reset();
      setLoading(false);
    }
    return (
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(handleSubmit)}
          className="w-full mb-8 space-y-8 py-8 w-5/6 mx-auto p-4 border rounded-lg shadow-md bg-white"
        >
          <FormField
            control={form.control}
            name="content"
            render={({ field }) => (
              <FormItem>
                <FormControl>
                  <Textarea
                    placeholder="What's up with your new work?"
                    className="h-20"
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <Button type="submit" disabled={loading}>
            {loading ? "Submitting..." : "Submit"}
          </Button>
        </form>
      </Form>
    );
  }